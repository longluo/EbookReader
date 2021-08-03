package com.longluo.zlibrary.text.model;

class ZLTextSpecialParagraphImpl extends ZLTextParagraphImpl {
	private final byte myKind;

	ZLTextSpecialParagraphImpl(byte kind, ZLTextPlainModel model, int offset) {
		super(model, offset);
		myKind = kind;
	}

	public byte getKind() {
		return myKind;
	}
}
