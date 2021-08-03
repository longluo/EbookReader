package com.longluo.ebookreader.ui.widget.text

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.longluo.ebookreader.lib.theme.secondaryTextColor

/**
 * @author Aidan Follestad (afollestad)
 */
class SecondaryTextView(context: Context, attrs: AttributeSet) :
    AppCompatTextView(context, attrs) {

    init {
        setTextColor(context.secondaryTextColor)
    }
}
