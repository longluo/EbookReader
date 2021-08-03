package com.longluo.ebookreader.ui.widget.text

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.longluo.ebookreader.lib.theme.ATH
import com.longluo.ebookreader.lib.theme.ThemeStore

/**
 * @author Aidan Follestad (afollestad)
 */
class EditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    init {
        if (!isInEditMode) {
            ATH.setTint(this, ThemeStore.accentColor(context))
        }
    }
}
