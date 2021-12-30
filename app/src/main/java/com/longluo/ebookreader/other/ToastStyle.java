package com.longluo.ebookreader.other;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;

import com.hjq.toast.style.BlackToastStyle;
import com.longluo.ebookreader.R;

/**
 * Toast 样式配置
 */
public final class ToastStyle extends BlackToastStyle {

    @Override
    protected Drawable getBackgroundDrawable(Context context) {
        GradientDrawable drawable = new GradientDrawable();
        // 设置颜色
        drawable.setColor(0X88000000);
        // 设置圆角
        drawable.setCornerRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) context.getResources().getDimension(R.dimen.button_circle_size), context.getResources().getDisplayMetrics()));
        return drawable;
    }

    @Override
    protected float getTextSize(Context context) {
        return context.getResources().getDimension(R.dimen.sp_14);
    }

    @Override
    protected int getHorizontalPadding(Context context) {
        return (int) context.getResources().getDimension(R.dimen.sp_24);
    }

    @Override
    protected int getVerticalPadding(Context context) {
        return (int) context.getResources().getDimension(R.dimen.sp_16);
    }
}