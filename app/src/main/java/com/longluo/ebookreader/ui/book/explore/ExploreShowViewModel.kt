package com.longluo.ebookreader.ui.book.explore

import android.app.Application
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.longluo.ebookreader.base.BaseViewModel
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.data.entities.BookSource
import com.longluo.ebookreader.data.entities.SearchBook
import com.longluo.ebookreader.model.webBook.WebBook
import com.longluo.ebookreader.utils.msg
import kotlinx.coroutines.Dispatchers.IO

class ExploreShowViewModel(application: Application) : BaseViewModel(application) {

    val booksData = MutableLiveData<List<SearchBook>>()
    val errorLiveData = MutableLiveData<String>()
    private var bookSource: BookSource? = null
    private var exploreUrl: String? = null
    private var page = 1

    fun initData(intent: Intent) {
        execute {
            val sourceUrl = intent.getStringExtra("sourceUrl")
            exploreUrl = intent.getStringExtra("exploreUrl")
            if (bookSource == null && sourceUrl != null) {
                bookSource = appDb.bookSourceDao.getBookSource(sourceUrl)
            }
            explore()
        }
    }

    fun explore() {
        val source = bookSource
        val url = exploreUrl
        if (source != null && url != null) {
            WebBook(source).exploreBook(viewModelScope, url, page)
                .timeout(30000L)
                .onSuccess(IO) { searchBooks ->
                    booksData.postValue(searchBooks)
                    appDb.searchBookDao.insert(*searchBooks.toTypedArray())
                    page++
                }.onError {
                    it.printStackTrace()
                    errorLiveData.postValue(it.msg)
                }
        }
    }

}