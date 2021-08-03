package com.longluo.ebookreader.lib.webdav

object HttpAuth {

    var auth: Auth? = null

    class Auth internal constructor(val user: String, val pass: String)

}