package com.longluo.ebookreader.lib.theme.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import com.longluo.ebookreader.lib.theme.ATH
import com.longluo.ebookreader.lib.theme.accentColor

/**
 * @author Aidan Follestad (afollestad)
 */
class ATERadioButton(context: Context, attrs: AttributeSet) : AppCompatRadioButton(context, attrs) {

    init {
        if (!isInEditMode) {
            ATH.setTint(this@ATERadioButton, context.accentColor)
        }
    }
}
