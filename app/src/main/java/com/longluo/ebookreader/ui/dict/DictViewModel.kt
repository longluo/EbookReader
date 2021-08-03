package com.longluo.ebookreader.ui.dict

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.longluo.ebookreader.base.BaseViewModel
import com.longluo.ebookreader.help.http.get
import com.longluo.ebookreader.help.http.newCallStrResponse
import com.longluo.ebookreader.help.http.okHttpClient
import com.longluo.ebookreader.utils.toastOnUi
import org.jsoup.Jsoup

class DictViewModel(application: Application) : BaseViewModel(application) {

    var dictHtmlData: MutableLiveData<String> = MutableLiveData()

    fun dict(word: String) {
        execute {
            val body = okHttpClient.newCallStrResponse {
                get("http://apii.dict.cn/mini.php", mapOf(Pair("q", word)))
            }.body
            val jsoup = Jsoup.parse(body!!)
            jsoup.body()
        }.onSuccess {
            dictHtmlData.postValue(it.html())
        }.onError {
            context.toastOnUi(it.localizedMessage)
        }

    }

}