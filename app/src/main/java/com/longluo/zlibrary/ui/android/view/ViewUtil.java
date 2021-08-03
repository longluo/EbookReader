package com.longluo.zlibrary.ui.android.view;

import android.graphics.*;

public abstract class ViewUtil {
	public static void setColorLevel(Paint paint, Integer level) {
		if (level != null) {
			paint.setColorFilter(new PorterDuffColorFilter(
				Color.rgb(level, level, level), PorterDuff.Mode.MULTIPLY
			));
		} else {
			paint.setColorFilter(null);
		}
	}
}
