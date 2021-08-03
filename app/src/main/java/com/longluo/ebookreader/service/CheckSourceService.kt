package com.longluo.ebookreader.service

import android.content.Intent
import androidx.core.app.NotificationCompat
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.BaseService
import com.longluo.ebookreader.constant.AppConst
import com.longluo.ebookreader.constant.EventBus
import com.longluo.ebookreader.constant.IntentAction
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.data.entities.BookSource
import com.longluo.ebookreader.help.AppConfig
import com.longluo.ebookreader.help.IntentHelp
import com.longluo.ebookreader.help.coroutine.CompositeCoroutine
import com.longluo.ebookreader.model.webBook.WebBook
import com.longluo.ebookreader.service.help.CheckSource
import com.longluo.ebookreader.ui.book.source.manage.BookSourceActivity
import com.longluo.ebookreader.utils.postEvent
import com.longluo.ebookreader.utils.toastOnUi
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import kotlin.math.min

class CheckSourceService : BaseService() {
    private var threadCount = AppConfig.threadCount
    private var searchCoroutine = Executors.newFixedThreadPool(min(threadCount,8)).asCoroutineDispatcher()
    private var tasks = CompositeCoroutine()
    private val allIds = ArrayList<String>()
    private val checkedIds = ArrayList<String>()
    private var processIndex = 0
    private var notificationMsg = ""
    private val notificationBuilder by lazy {
        NotificationCompat.Builder(this, AppConst.channelIdReadAloud)
            .setSmallIcon(R.drawable.ic_network_check)
            .setOngoing(true)
            .setContentTitle(getString(R.string.check_book_source))
            .setContentIntent(
                IntentHelp.activityPendingIntent<BookSourceActivity>(this, "activity")
            )
            .addAction(
                R.drawable.ic_stop_black_24dp,
                getString(R.string.cancel),
                IntentHelp.servicePendingIntent<CheckSourceService>(this, IntentAction.stop)
            )
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    }

    override fun onCreate() {
        super.onCreate()
        notificationMsg = getString(R.string.start)
        upNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            IntentAction.start -> intent.getStringArrayListExtra("selectIds")?.let {
                check(it)
            }
            else -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        tasks.clear()
        searchCoroutine.close()
        postEvent(EventBus.CHECK_SOURCE_DONE, 0)
    }

    private fun check(ids: List<String>) {
        if (allIds.isNotEmpty()) {
            toastOnUi("已有书源在校验,等完成后再试")
            return
        }
        tasks.clear()
        allIds.clear()
        checkedIds.clear()
        allIds.addAll(ids)
        processIndex = 0
        threadCount = min(allIds.size, threadCount)
        notificationMsg = getString(R.string.progress_show, "", 0, allIds.size)
        upNotification()
        for (i in 0 until threadCount) {
            check()
        }
    }

    /**
     * 检测
     */
    private fun check() {
        val index = processIndex
        synchronized(this) {
            processIndex++
        }
        execute(context = searchCoroutine) {
            if (index < allIds.size) {
                val sourceUrl = allIds[index]
                appDb.bookSourceDao.getBookSource(sourceUrl)?.let { source ->
                    check(source)
                } ?: onNext(sourceUrl, "")
            }
        }
    }

    fun check(source: BookSource) {
        execute(context = searchCoroutine) {
            val webBook = WebBook(source)
            var books = webBook.searchBookAwait(this, CheckSource.keyword)
            if (books.isEmpty()) {
                val exs = source.exploreKinds
                if (exs.isEmpty()) {
                    throw Exception("搜索内容为空并且没有发现")
                }
                var url: String? = null
                for (ex in exs) {
                    url = ex.url
                    if (!url.isNullOrBlank()) {
                        break
                    }
                }
                books = webBook.exploreBookAwait(this, url!!)
            }
            val book = webBook.getBookInfoAwait(this, books.first().toBook())
            val toc = webBook.getChapterListAwait(this, book)
            val content = webBook.getContentAwait(this, book, toc.first())
            if (content.isBlank()) {
                throw Exception("正文内容为空")
            }
        }.timeout(180000L)
            .onError(searchCoroutine) {
                source.addGroup("失效")
                source.bookSourceComment = """
                    "error:${it.localizedMessage}
                    ${source.bookSourceComment}"
                """.trimIndent()
                appDb.bookSourceDao.update(source)
            }.onSuccess(searchCoroutine) {
                source.removeGroup("失效")
                source.bookSourceComment = source.bookSourceComment
                    ?.split("\n")
                    ?.filterNot {
                        it.startsWith("error:")
                    }?.joinToString("\n")
                appDb.bookSourceDao.update(source)
            }.onFinally(searchCoroutine) {
                onNext(source.bookSourceUrl, source.bookSourceName)
            }
    }

    private fun onNext(sourceUrl: String, sourceName: String) {
        synchronized(this) {
            check()
            checkedIds.add(sourceUrl)
            notificationMsg =
                getString(R.string.progress_show, sourceName, checkedIds.size, allIds.size)
            upNotification()
            if (processIndex >= allIds.size + threadCount - 1) {
                stopSelf()
            }
        }
    }

    /**
     * 更新通知
     */
    private fun upNotification() {
        notificationBuilder.setContentText(notificationMsg)
        notificationBuilder.setProgress(allIds.size, checkedIds.size, false)
        postEvent(EventBus.CHECK_SOURCE, notificationMsg)
        startForeground(112202, notificationBuilder.build())
    }

}