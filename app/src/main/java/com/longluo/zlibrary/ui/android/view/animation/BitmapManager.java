package com.longluo.zlibrary.ui.android.view.animation;

import android.graphics.*;

import com.longluo.zlibrary.core.view.ZLViewEnums;

public interface BitmapManager {
	Bitmap getBitmap(ZLViewEnums.PageIndex index);
	void drawBitmap(Canvas canvas, int x, int y, ZLViewEnums.PageIndex index, Paint paint);
}
