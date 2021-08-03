package com.longluo.ebookreader.ui.book.arrange

import android.app.Application
import com.longluo.ebookreader.base.BaseViewModel
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.data.entities.Book


class ArrangeBookViewModel(application: Application) : BaseViewModel(application) {

    fun upCanUpdate(books: Array<Book>, canUpdate: Boolean) {
        execute {
            books.forEach {
                it.canUpdate = canUpdate
            }
            appDb.bookDao.update(*books)
        }
    }

    fun updateBook(vararg book: Book) {
        execute {
            appDb.bookDao.update(*book)
        }
    }

    fun deleteBook(vararg book: Book) {
        execute {
            appDb.bookDao.delete(*book)
        }
    }

}