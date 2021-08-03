package com.longluo.ebookreader.ui.rss.article

import android.content.Context
import androidx.viewbinding.ViewBinding
import com.longluo.ebookreader.base.adapter.RecyclerAdapter
import com.longluo.ebookreader.data.entities.RssArticle


abstract class BaseRssArticlesAdapter<VB : ViewBinding>(context: Context, val callBack: CallBack) :
    RecyclerAdapter<RssArticle, VB>(context) {

    interface CallBack {
        val isGridLayout: Boolean
        fun readRss(rssArticle: RssArticle)
    }
}