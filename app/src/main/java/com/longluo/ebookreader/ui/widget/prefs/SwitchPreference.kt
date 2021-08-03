package com.longluo.ebookreader.ui.widget.prefs

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import androidx.preference.PreferenceViewHolder
import androidx.preference.SwitchPreferenceCompat
import com.longluo.ebookreader.R
import com.longluo.ebookreader.lib.theme.ATH
import com.longluo.ebookreader.lib.theme.accentColor

class SwitchPreference(context: Context, attrs: AttributeSet) :
    SwitchPreferenceCompat(context, attrs) {

    private val isBottomBackground: Boolean

    init {
        layoutResource = R.layout.view_preference
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Preference)
        isBottomBackground = typedArray.getBoolean(R.styleable.Preference_isBottomBackground, false)
        typedArray.recycle()
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        val v = Preference.bindView<SwitchCompat>(
            context,
            holder,
            icon,
            title,
            summary,
            widgetLayoutResource,
            R.id.switchWidget,
            isBottomBackground = isBottomBackground
        )
        if (v is SwitchCompat && !v.isInEditMode) {
            ATH.setTint(v, context.accentColor)
        }
        super.onBindViewHolder(holder)
    }

}
