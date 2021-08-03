package com.longluo.ebookreader.service.help

import android.content.Context
import com.longluo.ebookreader.R
import com.longluo.ebookreader.constant.IntentAction
import com.longluo.ebookreader.data.entities.BookSource
import com.longluo.ebookreader.service.CheckSourceService
import com.longluo.ebookreader.utils.startService
import com.longluo.ebookreader.utils.toastOnUi

object CheckSource {
    var keyword = "我的"

    fun start(context: Context, sources: List<BookSource>) {
        if (sources.isEmpty()) {
            context.toastOnUi(R.string.non_select)
            return
        }
        val selectedIds: ArrayList<String> = arrayListOf()
        sources.map {
            selectedIds.add(it.bookSourceUrl)
        }
        context.startService<CheckSourceService> {
            action = IntentAction.start
            putExtra("selectIds", selectedIds)
        }
    }

    fun stop(context: Context) {
        context.startService<CheckSourceService> {
            action = IntentAction.stop
        }
    }
}