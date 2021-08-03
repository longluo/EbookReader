package com.longluo.ebookreader.lib.permission

interface OnPermissionsResultCallback {

    fun onPermissionsGranted()

    fun onPermissionsDenied(deniedPermissions: Array<String>)

}