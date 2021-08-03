package com.longluo.ebookreader.ui.book.read.config

import android.app.Application
import android.net.Uri
import com.longluo.ebookreader.base.BaseViewModel
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.data.entities.HttpTTS
import com.longluo.ebookreader.help.DefaultData
import com.longluo.ebookreader.help.http.newCall
import com.longluo.ebookreader.help.http.okHttpClient
import com.longluo.ebookreader.help.http.text
import com.longluo.ebookreader.utils.*

class SpeakEngineViewModel(application: Application) : BaseViewModel(application) {

    fun importDefault() {
        execute {
            DefaultData.importDefaultHttpTTS()
        }
    }

    fun importOnLine(url: String) {
        execute {
            okHttpClient.newCall {
                url(url)
            }.text("utf-8").let { json ->
                import(json)
            }
        }.onSuccess {
            context.toastOnUi("导入成功")
        }.onError {
            context.toastOnUi("导入失败")
        }
    }

    fun importLocal(uri: Uri) {
        execute {
            uri.readText(context)?.let {
                import(it)
            }
        }.onSuccess {
            context.toastOnUi("导入成功")
        }.onError {
            context.toastOnUi("导入失败")
        }
    }

    fun import(text: String) {
        when {
            text.isJsonArray() -> {
                GSON.fromJsonArray<HttpTTS>(text)?.let {
                    appDb.httpTTSDao.insert(*it.toTypedArray())
                }
            }
            text.isJsonObject() -> {
                GSON.fromJsonObject<HttpTTS>(text)?.let {
                    appDb.httpTTSDao.insert(it)
                }
            }
            else -> {
                throw Exception("格式不对")
            }
        }
    }

    fun export(uri: Uri) {
        execute {
            val httpTTS = appDb.httpTTSDao.all
            uri.writeBytes(context, "httpTts.json", GSON.toJson(httpTTS).toByteArray())
        }
    }
}