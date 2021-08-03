package com.longluo.ebookreader.ui.association

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jayway.jsonpath.JsonPath
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.BaseViewModel
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.data.entities.BookSource
import com.longluo.ebookreader.help.AppConfig
import com.longluo.ebookreader.help.ContentProcessor
import com.longluo.ebookreader.help.SourceHelp
import com.longluo.ebookreader.help.http.newCall
import com.longluo.ebookreader.help.http.okHttpClient
import com.longluo.ebookreader.help.http.text
import com.longluo.ebookreader.help.storage.OldRule
import com.longluo.ebookreader.help.storage.Restore
import com.longluo.ebookreader.utils.isAbsUrl
import com.longluo.ebookreader.utils.isJsonArray
import com.longluo.ebookreader.utils.isJsonObject

class ImportBookSourceViewModel(app: Application) : BaseViewModel(app) {
    var groupName: String? = null
    val errorLiveData = MutableLiveData<String>()
    val successLiveData = MutableLiveData<Int>()

    val allSources = arrayListOf<BookSource>()
    val checkSources = arrayListOf<BookSource?>()
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
            val selectSource = arrayListOf<BookSource>()
            selectStatus.forEachIndexed { index, b ->
                if (b) {
                    val source = allSources[index]
                    if (keepName) {
                        checkSources[index]?.let {
                            source.bookSourceName = it.bookSourceName
                            source.bookSourceGroup = it.bookSourceGroup
                            source.customOrder = it.customOrder
                        }
                    }
                    if (groupName != null) {
                        source.bookSourceGroup = groupName
                    }
                    selectSource.add(source)
                }
            }
            SourceHelp.insertBookSource(*selectSource.toTypedArray())
            ContentProcessor.upReplaceRules()
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
                        OldRule.jsonToBookSource(mText)?.let {
                            allSources.add(it)
                        }
                    }
                }
                mText.isJsonArray() -> {
                    val items: List<Map<String, Any>> = Restore.jsonPath.parse(mText).read("$")
                    for (item in items) {
                        val jsonItem = Restore.jsonPath.parse(item)
                        OldRule.jsonToBookSource(jsonItem.jsonString())?.let {
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
            it.printStackTrace()
            errorLiveData.postValue(it.localizedMessage ?: "")
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
                OldRule.jsonToBookSource(jsonItem.jsonString())?.let { source ->
                    allSources.add(source)
                }
            }
        }
    }

    private fun comparisonSource() {
        execute {
            allSources.forEach {
                val source = appDb.bookSourceDao.getBookSource(it.bookSourceUrl)
                checkSources.add(source)
                selectStatus.add(source == null || source.lastUpdateTime < it.lastUpdateTime)
            }
            successLiveData.postValue(allSources.size)
        }
    }

}