package com.longluo.ebookreader.model.rss

import com.longluo.ebookreader.data.entities.RssArticle

data class RssResult(val articles: MutableList<RssArticle>, val nextPageUrl: String?)