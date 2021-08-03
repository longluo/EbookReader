package com.longluo.zlibrary.text.view;

import com.longluo.zlibrary.core.image.ZLImageData;

public final class ZLTextImageElement extends ZLTextElement {
	public final String Id;
	public final ZLImageData ImageData;
	public final String URL;
	public final boolean IsCover;

	ZLTextImageElement(String id, ZLImageData imageData, String url, boolean isCover) {
		Id = id;
		ImageData = imageData;
		URL = url;
		IsCover = isCover;
	}
}
