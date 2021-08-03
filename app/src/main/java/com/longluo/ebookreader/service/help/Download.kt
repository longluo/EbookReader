package com.longluo.ebookreader.service.help

import android.content.Context
import com.longluo.ebookreader.constant.IntentAction
import com.longluo.ebookreader.service.DownloadService
import com.longluo.ebookreader.utils.startService

object Download {

    fun start(context: Context, downloadId: Long, fileName: String) {
        context.startService<DownloadService> {
            action = IntentAction.start
            putExtra("downloadId", downloadId)
            putExtra("fileName", fileName)
        }
    }

    fun stop(context: Context) {
        context.startService<DownloadService> {
            action = IntentAction.stop
        }
    }

}