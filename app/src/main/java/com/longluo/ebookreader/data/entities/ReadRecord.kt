package com.longluo.ebookreader.data.entities

import androidx.room.Entity

@Entity(tableName = "readRecord", primaryKeys = ["deviceId", "bookName"])
data class ReadRecord(
    var deviceId: String = "",
    var bookName: String = "",
    var readTime: Long = 0L
)