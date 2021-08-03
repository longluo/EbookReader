package com.longluo.zlibrary.text.view;

public class ZLTextImageRegionSoul extends ZLTextRegion.Soul {
	public final ZLTextImageElement ImageElement;

	ZLTextImageRegionSoul(ZLTextPosition position, ZLTextImageElement imageElement) {
		super(position.getParagraphIndex(), position.getElementIndex(), position.getElementIndex());
		ImageElement = imageElement;
	}
}
