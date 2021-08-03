package com.longluo.ebookreader.ui.association

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jayway.jsonpath.JsonPath
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.BaseViewModel
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.data.entities.RssSource
import com.longluo.ebookreader.help.AppConfig
import com.longluo.ebookreader.help.SourceHelp
import com.longluo.ebookreader.help.http.newCall
import com.longluo.ebookreader.help.http.okHttpClient
import com.longluo.ebookreader.help.http.text
import com.longluo.ebookreader.help.storage.Restore
import com.longluo.ebookreader.utils.*

class ImportRssSourceViewModel(app: Application) : BaseViewModel(app) {
    var groupName: String? = null
    val errorLiveData = MutableLiveData<String>()
    val successLiveData = MutableLiveData<Int>()

    val allSources = arrayListOf<RssSource>()
    val checkSources = arrayListOf<RssSource?>()
    val selectStatus = arrayListOf<Boolean>()

    fun isSelectAll(): Boolean {
        selectStatus.forEach {
            if (!it) {
                return false
            }
        }
        return true
    }

    fun selectCount(): Int {
        var count = 0
        selectStatus.forEach {
            if (it) {
                count++
            }
        }
        return count
    }

    fun importSelect(finally: () -> Unit) {
        execute {
            val keepName = AppConfig.importKeepName
            val selectSource = arrayListOf<RssSource>()
            selectStatus.forEachIndexed { index, b ->
                if (b) {
                    val source = allSources[index]
                    if (keepName) {
                        checkSources[index]?.let {
                            source.sourceName = it.sourceName
                            source.sourceGroup = it.sourceGroup
                            source.customOrder = it.customOrder
                        }
                    }
                    if (groupName != null) {
                        source.sourceGroup = groupName
                    }
                    selectSource.add(source)
                }
            }
            SourceHelp.insertRssSource(*selectSource.toTypedArray())
        }.onFinally {
            finally.invoke()
        }
    }

    fun importSource(text: String) {
        execute {
            val mText = text.trim()
            when {
                mText.isJsonObject() -> {
                    val json = JsonPath.parse(mText)
                    val urls = json.read<List<String>>("$.sourceUrls")
                    if (!urls.isNullOrEmpty()) {
                        urls.forEach {
                            importSourceUrl(it)
                        }
                    } else {
                        GSON.fromJsonArray<RssSource>(mText)?.let {
                            allSources.addAll(it)
                        }
                    }
                }
                mText.isJsonArray() -> {
                    val items: List<Map<String, Any>> = Restore.jsonPath.parse(mText).read("$")
                    for (item in items) {
                        val jsonItem = Restore.jsonPath.parse(item)
                        GSON.fromJsonObject<RssSource>(jsonItem.jsonString())?.let {
                            allSources.add(it)
                        }
                    }
                }
                mText.isAbsUrl() -> {
                    importSourceUrl(mText)
                }
                else -> throw Exception(context.getString(R.string.wrong_format))
            }
        }.onError {
            errorLiveData.postValue("ImportError:${it.localizedMessage}")
        }.onSuccess {
            comparisonSource()
        }
    }

    private suspend fun importSourceUrl(url: String) {
        okHttpClient.newCall {
            url(url)
        }.text("utf-8").let { body ->
            val items: List<Map<String, Any>> = Restore.jsonPath.parse(body).read("$")
            for (item in items) {
                val jsonItem = Restore.jsonPath.parse(item)
                GSON.fromJsonObject<RssSource>(jsonItem.jsonString())?.let { source ->
                    allSources.add(source)
                }
            }
        }
    }

    private fun comparisonSource() {
        execute {
            allSources.forEach {
                val has = appDb.rssSourceDao.getByKey(it.sourceUrl)
                checkSources.add(has)
                selectStatus.add(has == null)
            }
            successLiveData.postValue(allSources.size)
        }
    }

}