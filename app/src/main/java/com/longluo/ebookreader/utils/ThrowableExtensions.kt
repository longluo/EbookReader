package com.longluo.ebookreader.utils

val Throwable.msg: String
    get() {
        val stackTrace = stackTraceToString()
        val lMsg = this.localizedMessage ?: "noErrorMsg"
        return when {
            stackTrace.isNotEmpty() -> stackTrace
            else -> lMsg
        }
    }