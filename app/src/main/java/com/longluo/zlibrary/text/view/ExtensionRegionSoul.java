package com.longluo.zlibrary.text.view;

public class ExtensionRegionSoul extends ZLTextRegion.Soul {
	public final ExtensionElement Element;

	ExtensionRegionSoul(ZLTextPosition position, ExtensionElement element) {
		super(position.getParagraphIndex(), position.getElementIndex(), position.getElementIndex());
		Element = element;
	}
}
