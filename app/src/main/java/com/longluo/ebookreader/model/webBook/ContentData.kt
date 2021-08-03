package com.longluo.ebookreader.model.webBook

data class ContentData<T>(
    var content: String = "",
    var nextUrl: T
)