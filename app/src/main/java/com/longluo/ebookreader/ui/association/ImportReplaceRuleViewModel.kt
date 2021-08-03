package com.longluo.ebookreader.ui.association

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.longluo.ebookreader.base.BaseViewModel
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.data.entities.ReplaceRule
import com.longluo.ebookreader.help.AppConfig
import com.longluo.ebookreader.help.http.newCall
import com.longluo.ebookreader.help.http.okHttpClient
import com.longluo.ebookreader.help.http.text
import com.longluo.ebookreader.help.storage.OldReplace
import com.longluo.ebookreader.utils.isAbsUrl

class ImportReplaceRuleViewModel(app: Application) : BaseViewModel(app) {
    val errorLiveData = MutableLiveData<String>()
    val successLiveData = MutableLiveData<Int>()

    val allRules = arrayListOf<ReplaceRule>()
    val checkRules = arrayListOf<ReplaceRule?>()
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

    fun import(text: String) {
        execute {
            if (text.isAbsUrl()) {
                okHttpClient.newCall {
                    url(text)
                }.text("utf-8").let {
                    val rules = OldReplace.jsonToReplaceRules(it)
                    allRules.addAll(rules)
                }
            } else {
                val rules = OldReplace.jsonToReplaceRules(text)
                allRules.addAll(rules)
            }
        }.onError {
            errorLiveData.postValue(it.localizedMessage ?: "ERROR")
        }.onSuccess {
            comparisonSource()
        }
    }

    fun importSelect(finally: () -> Unit) {
        execute {
            val keepName = AppConfig.importKeepName
            val selectRules = arrayListOf<ReplaceRule>()
            selectStatus.forEachIndexed { index, b ->
                if (b) {
                    val rule = allRules[index]
                    selectRules.add(rule)
                }
            }
            appDb.replaceRuleDao.insert(*selectRules.toTypedArray())
        }.onFinally {
            finally.invoke()
        }
    }

    private fun comparisonSource() {
        execute {
            allRules.forEach {
                checkRules.add(null)
                selectStatus.add(false)
            }
        }.onSuccess {
            successLiveData.postValue(allRules.size)
        }
    }
}