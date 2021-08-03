package com.longluo.zlibrary.text.view;

public class ZLTextVideoRegionSoul extends ZLTextRegion.Soul {
	public final ZLTextVideoElement VideoElement;

	ZLTextVideoRegionSoul(ZLTextPosition position, ZLTextVideoElement videoElement) {
		super(position.getParagraphIndex(), position.getElementIndex(), position.getElementIndex());
		VideoElement = videoElement;
	}
}
