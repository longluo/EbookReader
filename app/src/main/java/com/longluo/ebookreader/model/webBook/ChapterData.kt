package com.longluo.ebookreader.model.webBook

import com.longluo.ebookreader.data.entities.BookChapter

data class ChapterData<T>(
    var chapterList: List<BookChapter>? = null,
    var nextUrl: T
)