package com.longluo.zlibrary.ui.android.image;

import java.io.InputStream;
import java.io.IOException;

import android.graphics.*;

import com.longluo.zlibrary.core.image.ZLStreamImage;

final class InputStreamImageData extends ZLAndroidImageData {
	private final ZLStreamImage myImage;

	InputStreamImageData(ZLStreamImage image) {
		myImage = image;
	}

	protected Bitmap decodeWithOptions(BitmapFactory.Options options) {
		final InputStream stream = myImage.inputStream();
		if (stream == null) {
			return null;
		}

		final Bitmap bmp = BitmapFactory.decodeStream(stream, new Rect(), options);
		try {
			stream.close();
		} catch (IOException e) {
		}
		return bmp;
	}
}
