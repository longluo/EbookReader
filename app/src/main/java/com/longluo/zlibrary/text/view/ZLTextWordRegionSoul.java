package com.longluo.zlibrary.text.view;

public class ZLTextWordRegionSoul extends ZLTextRegion.Soul {
	public final ZLTextWord Word;

	ZLTextWordRegionSoul(ZLTextPosition position, ZLTextWord word) {
		super(position.getParagraphIndex(), position.getElementIndex(), position.getElementIndex());
		Word = word;
	}
}
