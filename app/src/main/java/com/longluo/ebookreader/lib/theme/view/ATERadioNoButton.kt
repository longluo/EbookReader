package com.longluo.ebookreader.lib.theme.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import com.longluo.ebookreader.R
import com.longluo.ebookreader.lib.theme.Selector
import com.longluo.ebookreader.lib.theme.accentColor
import com.longluo.ebookreader.lib.theme.bottomBackground
import com.longluo.ebookreader.lib.theme.getPrimaryTextColor
import com.longluo.ebookreader.utils.ColorUtils
import com.longluo.ebookreader.utils.dp
import com.longluo.ebookreader.utils.getCompatColor

/**
 * @author Aidan Follestad (afollestad)
 */
class ATERadioNoButton(context: Context, attrs: AttributeSet) :
    AppCompatRadioButton(context, attrs) {

    private val isBottomBackground: Boolean

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ATERadioNoButton)
        isBottomBackground =
            typedArray.getBoolean(R.styleable.ATERadioNoButton_isBottomBackground, false)
        typedArray.recycle()
        initTheme()
    }

    private fun initTheme() {
        when {
            isInEditMode -> Unit
            isBottomBackground -> {
                val isLight = ColorUtils.isColorLight(context.bottomBackground)
                val textColor = context.getPrimaryTextColor(isLight)
                background = Selector.shapeBuild()
                    .setCornerRadius(2.dp)
                    .setStrokeWidth(2.dp)
                    .setCheckedBgColor(context.accentColor)
                    .setCheckedStrokeColor(context.accentColor)
                    .setDefaultStrokeColor(textColor)
                    .create()
                setTextColor(
                    Selector.colorBuild()
                        .setDefaultColor(textColor)
                        .setCheckedColor(context.getPrimaryTextColor(ColorUtils.isColorLight(context.accentColor)))
                        .create()
                )
            }
            else -> {
                val textColor = context.getCompatColor(R.color.primaryText)
                background = Selector.shapeBuild()
                    .setCornerRadius(2.dp)
                    .setStrokeWidth(2.dp)
                    .setCheckedBgColor(context.accentColor)
                    .setCheckedStrokeColor(context.accentColor)
                    .setDefaultStrokeColor(textColor)
                    .create()
                setTextColor(
                    Selector.colorBuild()
                        .setDefaultColor(textColor)
                        .setCheckedColor(context.getPrimaryTextColor(ColorUtils.isColorLight(context.accentColor)))
                        .create()
                )
            }
        }

    }

}
