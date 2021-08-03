package com.longluo.ebookreader.data.entities

import androidx.room.Entity
import androidx.room.Ignore
import com.longluo.ebookreader.model.analyzeRule.RuleDataInterface
import com.longluo.ebookreader.utils.GSON
import com.longluo.ebookreader.utils.fromJsonObject
import kotlinx.parcelize.IgnoredOnParcel


@Entity(
    tableName = "rssStars",
    primaryKeys = ["origin", "link"]
)
data class RssStar(
    var origin: String = "",
    var sort: String = "",
    var title: String = "",
    var starTime: Long = 0,
    var link: String = "",
    var pubDate: String? = null,
    var description: String? = null,
    var content: String? = null,
    var image: String? = null,
    var variable: String? = null
) : RuleDataInterface {

    @delegate:Transient
    @delegate:Ignore
    @IgnoredOnParcel
    override val variableMap by lazy {
        GSON.fromJsonObject<HashMap<String, String>>(variable) ?: HashMap()
    }

    override fun putVariable(key: String, value: String) {
        variableMap[key] = value
        variable = GSON.toJson(variableMap)
    }

    fun toRssArticle() = RssArticle(
        origin = origin,
        sort = sort,
        title = title,
        link = link,
        pubDate = pubDate,
        description = description,
        content = content,
        image = image
    )
}