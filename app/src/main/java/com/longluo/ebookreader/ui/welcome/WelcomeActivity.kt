package com.longluo.ebookreader.ui.welcome

import android.content.Intent
import android.os.Bundle
import com.github.liuyueyi.quick.transfer.ChineseUtils
import com.longluo.ebookreader.base.BaseActivity
import com.longluo.ebookreader.constant.PreferKey
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.databinding.ActivityWelcomeBinding
import com.longluo.ebookreader.help.AppConfig
import com.longluo.ebookreader.help.coroutine.Coroutine
import com.longluo.ebookreader.help.storage.BookWebDav
import com.longluo.ebookreader.lib.theme.accentColor
import com.longluo.ebookreader.ui.book.read.ReadBookActivity
import com.longluo.ebookreader.ui.main.MainActivity
import com.longluo.ebookreader.utils.getPrefBoolean
import com.longluo.ebookreader.utils.startActivity
import com.longluo.ebookreader.utils.viewbindingdelegate.viewBinding
import java.util.concurrent.TimeUnit

open class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>() {

    override val binding by viewBinding(ActivityWelcomeBinding::inflate)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        binding.ivBook.setColorFilter(accentColor)
        binding.vwTitleLine.setBackgroundColor(accentColor)
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
        } else {
            init()
        }
    }

    private fun init() {
        Coroutine.async {
            val books = appDb.bookDao.all
            books.forEach { book ->
                BookWebDav.getBookProgress(book)?.let { bookProgress ->
                    if (bookProgress.durChapterIndex > book.durChapterIndex ||
                        (bookProgress.durChapterIndex == book.durChapterIndex &&
                                bookProgress.durChapterPos > book.durChapterPos)
                    ) {
                        book.durChapterIndex = bookProgress.durChapterIndex
                        book.durChapterPos = bookProgress.durChapterPos
                        book.durChapterTitle = bookProgress.durChapterTitle
                        book.durChapterTime = bookProgress.durChapterTime
                        appDb.bookDao.update(book)
                    }
                }
            }
        }
        Coroutine.async {
            appDb.cacheDao.clearDeadline(System.currentTimeMillis())
            //清除过期数据
            if (getPrefBoolean(PreferKey.autoClearExpired, true)) {
                appDb.searchBookDao
                    .clearExpired(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))
            }
            //初始化简繁转换引擎
            when (AppConfig.chineseConverterType) {
                1 -> ChineseUtils.t2s("初始化")
                2 -> ChineseUtils.s2t("初始化")
                else -> null
            }
        }
        binding.root.postDelayed({ startMainActivity() }, 500)
    }

    private fun startMainActivity() {
        startActivity<MainActivity>()
        if (getPrefBoolean(PreferKey.defaultToRead)) {
            startActivity<ReadBookActivity>()
        }
        finish()
    }

}

class Launcher1 : WelcomeActivity()
class Launcher2 : WelcomeActivity()
class Launcher3 : WelcomeActivity()
class Launcher4 : WelcomeActivity()
class Launcher5 : WelcomeActivity()
class Launcher6 : WelcomeActivity()