package com.longluo.ebookreader.service.help

import android.content.Context
import android.content.Intent
import com.longluo.ebookreader.constant.IntentAction
import com.longluo.ebookreader.constant.PreferKey
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.data.entities.HttpTTS
import com.longluo.ebookreader.service.BaseReadAloudService
import com.longluo.ebookreader.service.HttpReadAloudService
import com.longluo.ebookreader.service.TTSReadAloudService
import com.longluo.ebookreader.utils.getPrefLong
import splitties.init.appCtx

object ReadAloud {
    private var aloudClass: Class<*> = getReadAloudClass()
    var httpTTS: HttpTTS? = null

    private fun getReadAloudClass(): Class<*> {
        val spId = appCtx.getPrefLong(PreferKey.speakEngine)
        httpTTS = appDb.httpTTSDao.get(spId)
        return if (httpTTS != null) {
            HttpReadAloudService::class.java
        } else {
            TTSReadAloudService::class.java
        }
    }

    fun upReadAloudClass() {
        stop(appCtx)
        aloudClass = getReadAloudClass()
    }

    fun play(
        context: Context,
        title: String,
        subtitle: String,
        pageIndex: Int,
        dataKey: String,
        play: Boolean = true
    ) {
        val intent = Intent(context, aloudClass)
        intent.action = IntentAction.play
        intent.putExtra("title", title)
        intent.putExtra("subtitle", subtitle)
        intent.putExtra("pageIndex", pageIndex)
        intent.putExtra("dataKey", dataKey)
        intent.putExtra("play", play)
        context.startService(intent)
    }

    fun pause(context: Context) {
        if (BaseReadAloudService.isRun) {
            val intent = Intent(context, aloudClass)
            intent.action = IntentAction.pause
            context.startService(intent)
        }
    }

    fun resume(context: Context) {
        if (BaseReadAloudService.isRun) {
            val intent = Intent(context, aloudClass)
            intent.action = IntentAction.resume
            context.startService(intent)
        }
    }

    fun stop(context: Context) {
        if (BaseReadAloudService.isRun) {
            val intent = Intent(context, aloudClass)
            intent.action = IntentAction.stop
            context.startService(intent)
        }
    }

    fun prevParagraph(context: Context) {
        if (BaseReadAloudService.isRun) {
            val intent = Intent(context, aloudClass)
            intent.action = IntentAction.prevParagraph
            context.startService(intent)
        }
    }

    fun nextParagraph(context: Context) {
        if (BaseReadAloudService.isRun) {
            val intent = Intent(context, aloudClass)
            intent.action = IntentAction.nextParagraph
            context.startService(intent)
        }
    }

    fun upTtsSpeechRate(context: Context) {
        if (BaseReadAloudService.isRun) {
            val intent = Intent(context, aloudClass)
            intent.action = IntentAction.upTtsSpeechRate
            context.startService(intent)
        }
    }

    fun setTimer(context: Context, minute: Int) {
        if (BaseReadAloudService.isRun) {
            val intent = Intent(context, aloudClass)
            intent.action = IntentAction.setTimer
            intent.putExtra("minute", minute)
            context.startService(intent)
        }
    }

}