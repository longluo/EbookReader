package com.longluo.zlibrary.ui.android.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

final class BitmapImageData extends ZLAndroidImageData {
	static BitmapImageData get(ZLBitmapImage image) {
		final Bitmap bitmap = image.getBitmap();
		return bitmap != null ? new BitmapImageData(bitmap) : null;
	}

	private final Bitmap myBitmap;

	private BitmapImageData(Bitmap bitmap) {
		myBitmap = bitmap;
	}

	protected Bitmap decodeWithOptions(BitmapFactory.Options options) {
		final int scaleFactor = options.inSampleSize;
		if (scaleFactor <= 1) {
			return myBitmap;
		}
		try {
			return Bitmap.createScaledBitmap(
				myBitmap, myBitmap.getWidth() / scaleFactor, myBitmap.getHeight() / scaleFactor, false
			);
		} catch (Exception e) {
			return null;
		}
	}
}
