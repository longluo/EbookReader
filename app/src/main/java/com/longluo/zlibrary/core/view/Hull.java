package com.longluo.zlibrary.core.view;

public interface Hull {
	interface DrawMode {
		int None = 0;
		int Outline = 1;
		int Fill = 2;
	};

	void draw(ZLPaintContext context, int mode);
	int distanceTo(int x, int y);
	boolean isBefore(int x, int y);
}
