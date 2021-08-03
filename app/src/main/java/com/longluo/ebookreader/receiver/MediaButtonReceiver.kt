package com.longluo.ebookreader.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import com.longluo.ebookreader.constant.EventBus
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.help.AppConfig
import com.longluo.ebookreader.help.LifecycleHelp
import com.longluo.ebookreader.service.AudioPlayService
import com.longluo.ebookreader.service.BaseReadAloudService
import com.longluo.ebookreader.service.help.AudioPlay
import com.longluo.ebookreader.service.help.ReadAloud
import com.longluo.ebookreader.ui.book.audio.AudioPlayActivity
import com.longluo.ebookreader.ui.book.read.ReadBookActivity
import com.longluo.ebookreader.ui.main.MainActivity
import com.longluo.ebookreader.utils.postEvent


/**
 * Created by GKF on 2018/1/6.
 * 监听耳机键
 */
class MediaButtonReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (handleIntent(context, intent) && isOrderedBroadcast) {
            abortBroadcast()
        }
    }

    companion object {

        fun handleIntent(context: Context, intent: Intent): Boolean {
            val intentAction = intent.action
            if (Intent.ACTION_MEDIA_BUTTON == intentAction) {
                val keyEvent = intent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)
                    ?: return false
                val keycode: Int = keyEvent.keyCode
                val action: Int = keyEvent.action
                if (action == KeyEvent.ACTION_DOWN) {
                    when (keycode) {
                        KeyEvent.KEYCODE_MEDIA_PREVIOUS -> {
                            ReadAloud.prevParagraph(context)
                        }
                        KeyEvent.KEYCODE_MEDIA_NEXT -> {
                            ReadAloud.nextParagraph(context)
                        }
                        else -> readAloud(context)
                    }
                }
            }
            return true
        }

        fun readAloud(context: Context, isMediaKey: Boolean = true) {
            when {
                BaseReadAloudService.isRun -> if (BaseReadAloudService.isPlay()) {
                    ReadAloud.pause(context)
                    AudioPlay.pause(context)
                } else {
                    ReadAloud.resume(context)
                    AudioPlay.resume(context)
                }
                AudioPlayService.isRun -> if (AudioPlayService.pause) {
                    AudioPlay.resume(context)
                } else {
                    AudioPlay.pause(context)
                }
                LifecycleHelp.isExistActivity(ReadBookActivity::class.java) ->
                    postEvent(EventBus.MEDIA_BUTTON, true)
                LifecycleHelp.isExistActivity(AudioPlayActivity::class.java) ->
                    postEvent(EventBus.MEDIA_BUTTON, true)
                else -> if (AppConfig.mediaButtonOnExit || !isMediaKey) {
                    appDb.bookDao.lastReadBook?.let {
                        if (!LifecycleHelp.isExistActivity(MainActivity::class.java)) {
                            Intent(context, MainActivity::class.java).let {
                                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(it)
                            }
                        }
                        Intent(context, ReadBookActivity::class.java).let {
                            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            it.putExtra("readAloud", true)
                            context.startActivity(it)
                        }
                    }
                }
            }
        }
    }

}
