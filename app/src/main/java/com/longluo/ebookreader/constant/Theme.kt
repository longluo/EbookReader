package com.longluo.ebookreader.constant

import com.longluo.ebookreader.help.AppConfig
import com.longluo.ebookreader.utils.ColorUtils

enum class Theme {
    Dark, Light, Auto, Transparent, EInk;

    companion object {
        fun getTheme() = when {
            AppConfig.isEInkMode -> EInk
            AppConfig.isNightTheme -> Dark
            else -> Light
        }

        fun getTheme(backgroundColor: Int) =
            if (ColorUtils.isColorLight(backgroundColor)) Light
            else Dark

    }
}