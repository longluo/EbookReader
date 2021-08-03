package com.longluo.ebookreader.ui.widget.text

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.longluo.ebookreader.R
import com.longluo.ebookreader.lib.theme.Selector
import com.longluo.ebookreader.lib.theme.ThemeStore
import com.longluo.ebookreader.lib.theme.bottomBackground
import com.longluo.ebookreader.utils.ColorUtils
import com.longluo.ebookreader.utils.dp
import com.longluo.ebookreader.utils.getCompatColor

class AccentStrokeTextView(context: Context, attrs: AttributeSet) :
    AppCompatTextView(context, attrs) {

    private var radius = 3.dp
    private val isBottomBackground: Boolean

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AccentStrokeTextView)
        radius = typedArray.getDimensionPixelOffset(R.styleable.StrokeTextView_radius, radius)
        isBottomBackground =
            typedArray.getBoolean(R.styleable.StrokeTextView_isBottomBackground, false)
        typedArray.recycle()
        upStyle()
    }

    private fun upStyle() {
        val isLight = ColorUtils.isColorLight(context.bottomBackground)
        val disableColor = if (isBottomBackground) {
            if (isLight) {
                context.getCompatColor(R.color.md_light_disabled)
            } else {
                context.getCompatColor(R.color.md_dark_disabled)
            }
        } else {
            context.getCompatColor(R.color.disabled)
        }
        background = Selector.shapeBuild()
            .setCornerRadius(radius)
            .setStrokeWidth(1.dp)
            .setDisabledStrokeColor(disableColor)
            .setDefaultStrokeColor(ThemeStore.accentColor(context))
            .setPressedBgColor(context.getCompatColor(R.color.transparent30))
            .create()
        setTextColor(
            Selector.colorBuild()
                .setDefaultColor(ThemeStore.accentColor(context))
                .setDisabledColor(disableColor)
                .create()
        )
    }

}
