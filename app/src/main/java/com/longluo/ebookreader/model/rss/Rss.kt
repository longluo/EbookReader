package com.longluo.ebookreader.model.rss

import com.longluo.ebookreader.data.entities.RssArticle
import com.longluo.ebookreader.data.entities.RssSource
import com.longluo.ebookreader.help.coroutine.Coroutine
import com.longluo.ebookreader.model.Debug
import com.longluo.ebookreader.model.analyzeRule.AnalyzeRule
import com.longluo.ebookreader.model.analyzeRule.AnalyzeUrl
import com.longluo.ebookreader.model.analyzeRule.RuleData
import com.longluo.ebookreader.utils.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object Rss {

    fun getArticles(
        scope: CoroutineScope,
        sortName: String,
        sortUrl: String,
        rssSource: RssSource,
        page: Int,
        context: CoroutineContext = Dispatchers.IO
    ): Coroutine<RssResult> {
        return Coroutine.async(scope, context) {
            val ruleData = RuleData()
            val analyzeUrl = AnalyzeUrl(
                sortUrl,
                page = page,
                ruleData = ruleData,
                headerMapF = rssSource.getHeaderMap()
            )
            val body = analyzeUrl.getStrResponse(rssSource.sourceUrl).body
            RssParserByRule.parseXML(sortName, sortUrl, body, rssSource, ruleData)
        }
    }

    fun getContent(
        scope: CoroutineScope,
        rssArticle: RssArticle,
        ruleContent: String,
        rssSource: RssSource,
        context: CoroutineContext = Dispatchers.IO
    ): Coroutine<String> {
        return Coroutine.async(scope, context) {
            val analyzeUrl = AnalyzeUrl(
                rssArticle.link,
                baseUrl = rssArticle.origin,
                ruleData = rssArticle,
                headerMapF = rssSource.getHeaderMap()
            )
            val body = analyzeUrl.getStrResponse(rssArticle.origin).body
            Debug.log(rssSource.sourceUrl, "≡获取成功:${rssSource.sourceUrl}")
            Debug.log(rssSource.sourceUrl, body, state = 20)
            val analyzeRule = AnalyzeRule(rssArticle)
            analyzeRule.setContent(body)
                .setBaseUrl(NetworkUtils.getAbsoluteURL(rssArticle.origin, rssArticle.link))
            analyzeRule.getString(ruleContent)
        }
    }
}