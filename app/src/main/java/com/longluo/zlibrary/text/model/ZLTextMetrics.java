package com.longluo.zlibrary.text.model;

public final class ZLTextMetrics {
	public final int DPI;
	public final int FullWidth;
	public final int FullHeight;
	public final int FontSize;

	public ZLTextMetrics(int dpi, int fullWidth, int fullHeight, int fontSize) {
		DPI = dpi;
		FullWidth = fullWidth;
		FullHeight = fullHeight;
		FontSize = fontSize;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ZLTextMetrics)) {
			return false;
		}
		final ZLTextMetrics oo = (ZLTextMetrics)o;
		return
			DPI == oo.DPI &&
			FullWidth == oo.FullWidth &&
			FullHeight == oo.FullHeight;
	}

	@Override
	public int hashCode() {
		return DPI + 13 * (FullHeight + 13 * FullWidth);
	}
}
