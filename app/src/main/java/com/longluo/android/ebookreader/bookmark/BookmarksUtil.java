package com.longluo.android.ebookreader.bookmark;

import yuku.ambilwarna.widget.AmbilWarnaPrefWidgetView;

import com.longluo.zlibrary.core.util.ZLColor;
import com.longluo.zlibrary.ui.android.util.ZLAndroidColorUtil;
import com.longluo.ebookreader.book.HighlightingStyle;

abstract class BookmarksUtil {
	static void setupColorView(AmbilWarnaPrefWidgetView colorView, HighlightingStyle style) {
		Integer rgb = null;
		if (style != null) {
			final ZLColor color = style.getBackgroundColor();
			if (color != null) {
				rgb = ZLAndroidColorUtil.rgb(color);
			}
		}

		if (rgb != null) {
			colorView.showCross(false);
			colorView.setBackgroundColor(rgb);
		} else {
			colorView.showCross(true);
			colorView.setBackgroundColor(0);
		}
	}
}
