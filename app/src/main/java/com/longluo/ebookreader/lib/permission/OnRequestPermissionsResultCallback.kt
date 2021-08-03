package com.longluo.ebookreader.lib.permission

interface OnRequestPermissionsResultCallback {

    fun onRequestPermissionsResult(permissions: Array<String>, grantResults: IntArray)

    fun onSettingActivityResult()
}
