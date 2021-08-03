package com.longluo.zlibrary.text.model;

import java.util.Map;

import com.longluo.zlibrary.core.image.ZLImage;

public final class ZLImageEntry {
	private final Map<String,ZLImage> myImageMap;
	public final String Id;
	public final short VOffset;
	public final boolean IsCover;

	ZLImageEntry(Map<String,ZLImage> imageMap, String id, short vOffset, boolean isCover) {
		myImageMap = imageMap;
		Id = id;
		VOffset = vOffset;
		IsCover = isCover;
	}

	public ZLImage getImage() {
		return myImageMap.get(Id);
	}
}
