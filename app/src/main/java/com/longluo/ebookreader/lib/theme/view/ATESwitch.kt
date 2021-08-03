package com.longluo.ebookreader.lib.theme.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import com.longluo.ebookreader.lib.theme.ATH
import com.longluo.ebookreader.lib.theme.accentColor

/**
 * @author Aidan Follestad (afollestad)
 */
class ATESwitch(context: Context, attrs: AttributeSet) : SwitchCompat(context, attrs) {

    init {
        if (!isInEditMode) {
            ATH.setTint(this, context.accentColor)
        }

    }

}
