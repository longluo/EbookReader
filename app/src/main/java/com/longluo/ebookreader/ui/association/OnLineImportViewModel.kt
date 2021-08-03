package com.longluo.ebookreader.ui.association

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.BaseViewModel
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.data.entities.HttpTTS
import com.longluo.ebookreader.data.entities.TxtTocRule
import com.longluo.ebookreader.help.ReadBookConfig
import com.longluo.ebookreader.help.ThemeConfig
import com.longluo.ebookreader.help.http.newCall
import com.longluo.ebookreader.help.http.okHttpClient
import com.longluo.ebookreader.help.http.text
import com.longluo.ebookreader.utils.GSON
import com.longluo.ebookreader.utils.fromJsonArray
import com.longluo.ebookreader.utils.fromJsonObject
import com.longluo.ebookreader.utils.isJsonArray
import okhttp3.MediaType.Companion.toMediaType

class OnLineImportViewModel(app: Application) : BaseViewModel(app) {
    val successLive = MutableLiveData<Pair<String, String>>()
    val errorLive = MutableLiveData<String>()

    fun getText(url: String, success: (text: String) -> Unit) {
        execute {
            okHttpClient.newCall {
                url(url)
            }.text("utf-8")
        }.onSuccess {
            success.invoke(it)
        }.onError {
            errorLive.postValue(
                it.localizedMessage ?: context.getString(R.string.unknown_error)
            )
        }
    }

    fun getBytes(url: String, success: (bytes: ByteArray) -> Unit) {
        execute {
            @Suppress("BlockingMethodInNonBlockingContext")
            okHttpClient.newCall {
                url(url)
            }.bytes()
        }.onSuccess {
            success.invoke(it)
        }.onError {
            errorLive.postValue(
                it.localizedMessage ?: context.getString(R.string.unknown_error)
            )
        }
    }

    fun importTextTocRule(json: String, finally: (title: String, msg: String) -> Unit) {
        execute {
            if (json.isJsonArray()) {
                GSON.fromJsonArray<TxtTocRule>(json)?.let {
                    appDb.txtTocRuleDao.insert(*it.toTypedArray())
                } ?: throw Exception("格式不对")
            } else {
                GSON.fromJsonObject<TxtTocRule>(json)?.let {
                    appDb.txtTocRuleDao.insert(it)
                } ?: throw Exception("格式不对")
            }
        }.onSuccess {
            finally.invoke(context.getString(R.string.success), "导入Txt规则成功")
        }.onError {
            finally.invoke(
                context.getString(R.string.error),
                it.localizedMessage ?: context.getString(R.string.unknown_error)
            )
        }
    }

    fun importHttpTTS(json: String, finally: (title: String, msg: String) -> Unit) {
        execute {
            if (json.isJsonArray()) {
                GSON.fromJsonArray<HttpTTS>(json)?.let {
                    appDb.httpTTSDao.insert(*it.toTypedArray())
                    return@execute it.size
                } ?: throw Exception("格式不对")
            } else {
                GSON.fromJsonObject<HttpTTS>(json)?.let {
                    appDb.httpTTSDao.insert(it)
                    return@execute 1
                } ?: throw Exception("格式不对")
            }
        }.onSuccess {
            finally.invoke(context.getString(R.string.success), "导入${it}朗读引擎")
        }.onError {
            finally.invoke(
                context.getString(R.string.error),
                it.localizedMessage ?: context.getString(R.string.unknown_error)
            )
        }
    }

    fun importTheme(json: String, finally: (title: String, msg: String) -> Unit) {
        execute {
            if (json.isJsonArray()) {
                GSON.fromJsonArray<ThemeConfig.Config>(json)?.forEach {
                    ThemeConfig.addConfig(it)
                } ?: throw Exception("格式不对")
            } else {
                GSON.fromJsonObject<ThemeConfig.Config>(json)?.let {
                    ThemeConfig.addConfig(it)
                } ?: throw Exception("格式不对")
            }
        }.onSuccess {
            finally.invoke(context.getString(R.string.success), "导入主题成功")
        }.onError {
            finally.invoke(
                context.getString(R.string.error),
                it.localizedMessage ?: context.getString(R.string.unknown_error)
            )
        }
    }

    fun importReadConfig(bytes: ByteArray, finally: (title: String, msg: String) -> Unit) {
        execute {
            val config = ReadBookConfig.import(bytes)
            ReadBookConfig.configList.forEachIndexed { index, c ->
                if (c.name == config.name) {
                    ReadBookConfig.configList[index] = config
                    return@execute config.name
                }
                ReadBookConfig.configList.add(config)
                return@execute config.name
            }
        }.onSuccess {
            finally.invoke(context.getString(R.string.success), "导入排版成功")
        }.onError {
            finally.invoke(
                context.getString(R.string.error),
                it.localizedMessage ?: context.getString(R.string.unknown_error)
            )
        }
    }

    fun determineType(url: String, finally: (title: String, msg: String) -> Unit) {
        execute {
            val rs = okHttpClient.newCall {
                url(url)
            }
            when (rs.contentType()) {
                "application/zip".toMediaType(),
                "application/octet-stream".toMediaType() -> {
                    @Suppress("BlockingMethodInNonBlockingContext")
                    importReadConfig(rs.bytes(), finally)
                }
                else -> {
                    val json = rs.text("utf-8")
                    when {
                        json.contains("bookSourceUrl") ->
                            successLive.postValue(Pair("bookSource", json))
                        json.contains("sourceUrl") ->
                            successLive.postValue(Pair("rssSource", json))
                        json.contains("replacement") ->
                            successLive.postValue(Pair("replaceRule", json))
                        json.contains("themeName") ->
                            importTextTocRule(json, finally)
                        json.contains("name") && json.contains("rule") ->
                            importTextTocRule(json, finally)
                        json.contains("name") && json.contains("url") ->
                            importTextTocRule(json, finally)
                    }
                }
            }
        }
    }

}