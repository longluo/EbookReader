package com.longluo.ebookreader.lib.theme.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar
import com.longluo.ebookreader.lib.theme.ATH
import com.longluo.ebookreader.lib.theme.ThemeStore

/**
 * @author Aidan Follestad (afollestad)
 */
class ATEProgressBar(context: Context, attrs: AttributeSet) : ProgressBar(context, attrs) {

    init {
        if (!isInEditMode) {
            ATH.setTint(this, ThemeStore.accentColor(context))
        }
    }
}