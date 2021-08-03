package com.longluo.ebookreader.ui.main

import android.app.Application
import com.longluo.ebookreader.base.BaseViewModel
import com.longluo.ebookreader.constant.BookType
import com.longluo.ebookreader.constant.EventBus
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.data.entities.Book
import com.longluo.ebookreader.help.AppConfig
import com.longluo.ebookreader.help.BookHelp
import com.longluo.ebookreader.help.DefaultData
import com.longluo.ebookreader.help.LocalConfig
import com.longluo.ebookreader.model.webBook.WebBook
import com.longluo.ebookreader.service.help.CacheBook
import com.longluo.ebookreader.utils.FileUtils
import com.longluo.ebookreader.utils.postEvent
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.Executors
import kotlin.math.min

class MainViewModel(application: Application) : BaseViewModel(application) {
    private var threadCount = AppConfig.threadCount
    private var upTocPool = Executors.newFixedThreadPool(min(threadCount,8)).asCoroutineDispatcher()
    val updateList = CopyOnWriteArraySet<String>()
    private val bookMap = ConcurrentHashMap<String, Book>()

    @Volatile
    private var usePoolCount = 0

    override fun onCleared() {
        super.onCleared()
        upTocPool.close()
    }

    fun upPool() {
        threadCount = AppConfig.threadCount
        upTocPool.close()
        upTocPool = Executors.newFixedThreadPool(min(threadCount,8)).asCoroutineDispatcher()
    }

    fun upAllBookToc() {
        execute {
            upToc(appDb.bookDao.hasUpdateBooks)
        }
    }

    fun upToc(books: List<Book>) {
        execute(context = upTocPool) {
            books.filter {
                it.origin != BookType.local && it.canUpdate
            }.forEach {
                bookMap[it.bookUrl] = it
            }
            for (i in 0 until threadCount) {
                if (usePoolCount < threadCount) {
                    usePoolCount++
                    updateToc()
                }
            }
        }
    }

    @Synchronized
    private fun updateToc() {
        var update = false
        bookMap.forEach { bookEntry ->
            if (!updateList.contains(bookEntry.key)) {
                update = true
                val book = bookEntry.value
                synchronized(this) {
                    updateList.add(book.bookUrl)
                    postEvent(EventBus.UP_BOOKSHELF, book.bookUrl)
                }
                appDb.bookSourceDao.getBookSource(book.origin)?.let { bookSource ->
                    execute(context = upTocPool) {
                        val webBook = WebBook(bookSource)
                        if (book.tocUrl.isBlank()) {
                            webBook.getBookInfoAwait(this, book)
                        }
                        val toc = webBook.getChapterListAwait(this, book)
                        appDb.bookDao.update(book)
                        appDb.bookChapterDao.delByBook(book.bookUrl)
                        appDb.bookChapterDao.insert(*toc.toTypedArray())
                        cacheBook(webBook, book)
                    }.onError(upTocPool) {
                        it.printStackTrace()
                    }.onFinally(upTocPool) {
                        synchronized(this) {
                            bookMap.remove(bookEntry.key)
                            updateList.remove(book.bookUrl)
                            postEvent(EventBus.UP_BOOKSHELF, book.bookUrl)
                            upNext()
                        }
                    }
                } ?: synchronized(this) {
                    bookMap.remove(bookEntry.key)
                    updateList.remove(book.bookUrl)
                    postEvent(EventBus.UP_BOOKSHELF, book.bookUrl)
                    upNext()
                }
                return
            }
        }
        if (!update) {
            usePoolCount--
        }
    }

    private fun cacheBook(webBook: WebBook, book: Book) {
        execute {
            if (book.totalChapterNum > book.durChapterIndex) {
                val downloadToIndex =
                    min(book.totalChapterNum, book.durChapterIndex.plus(AppConfig.preDownloadNum))
                for (i in book.durChapterIndex until downloadToIndex) {
                    appDb.bookChapterDao.getChapter(book.bookUrl, i)?.let { chapter ->
                        if (!BookHelp.hasContent(book, chapter)) {
                            var addToCache = false
                            while (!addToCache) {
                                if (CacheBook.downloadCount() < 10) {
                                    CacheBook.download(this, webBook, book, chapter)
                                    addToCache = true
                                } else {
                                    delay(100)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun upNext() {
        if (bookMap.size > updateList.size) {
            updateToc()
        } else {
            usePoolCount--
        }
    }

    fun postLoad() {
        execute {
            FileUtils.deleteFile(FileUtils.getPath(context.cacheDir, "Fonts"))
            if (appDb.httpTTSDao.count == 0) {
                DefaultData.httpTTS.let {
                    appDb.httpTTSDao.insert(*it.toTypedArray())
                }
            }
        }
    }

    fun upVersion() {
        execute {
            if (LocalConfig.needUpHttpTTS) {
                DefaultData.importDefaultHttpTTS()
            }
            if (LocalConfig.needUpTxtTocRule) {
                DefaultData.importDefaultTocRules()
            }
            if (LocalConfig.needUpRssSources) {
                DefaultData.importDefaultRssSources()
            }
        }
    }
}