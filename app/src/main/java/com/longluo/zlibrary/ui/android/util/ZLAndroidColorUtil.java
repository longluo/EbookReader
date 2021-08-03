package com.longluo.zlibrary.ui.android.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.longluo.zlibrary.core.util.ZLColor;

public abstract class ZLAndroidColorUtil {
	public static int rgba(ZLColor color, int alpha) {
		return color != null
			? Color.argb(alpha, color.Red, color.Green, color.Blue)
			: Color.argb(alpha, 0, 0, 0);
	}

	public static int rgb(ZLColor color) {
		return color != null ? Color.rgb(color.Red, color.Green, color.Blue) : 0;
	}

	public static ZLColor getAverageColor(Bitmap bitmap) {
		final int w = Math.min(bitmap.getWidth(), 7);
		final int h = Math.min(bitmap.getHeight(), 7);
		long r = 0, g = 0, b = 0;
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				int color = bitmap.getPixel(i, j);
				r += color & 0xFF0000;
				g += color & 0xFF00;
				b += color & 0xFF;
			}
		}
		r /= w * h;
		g /= w * h;
		b /= w * h;
		r >>= 16;
		g >>= 8;
		return new ZLColor((int)(r & 0xFF), (int)(g & 0xFF), (int)(b & 0xFF));
	}
}
