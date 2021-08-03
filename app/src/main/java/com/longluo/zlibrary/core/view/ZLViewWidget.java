package com.longluo.zlibrary.core.view;

public interface ZLViewWidget {
	void reset();
	void repaint();

	void startManualScrolling(int x, int y, ZLView.Direction direction);
	void scrollManuallyTo(int x, int y);
	void startAnimatedScrolling(ZLView.PageIndex pageIndex, int x, int y, ZLView.Direction direction, int speed);
	void startAnimatedScrolling(ZLView.PageIndex pageIndex, ZLView.Direction direction, int speed);
	void startAnimatedScrolling(int x, int y, int speed);

	void setScreenBrightness(int percent);
	int getScreenBrightness();
}
