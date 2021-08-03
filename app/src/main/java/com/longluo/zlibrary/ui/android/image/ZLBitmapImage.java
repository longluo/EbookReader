package com.longluo.zlibrary.ui.android.image;

import com.longluo.zlibrary.core.image.ZLImage;

import android.graphics.Bitmap;

public class ZLBitmapImage implements ZLImage {
	private final Bitmap myBitmap;

	public ZLBitmapImage(Bitmap bitmap) {
		myBitmap = bitmap;
	}

	public Bitmap getBitmap() {
		return myBitmap;
	}

	@Override
	public String getURI() {
		return "bitmap image";
	}
}
