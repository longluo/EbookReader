package com.longluo.zlibrary.text.model;

class ZLTextParagraphImpl implements ZLTextParagraph {
	private final ZLTextPlainModel myModel;
	private final int myIndex;

	ZLTextParagraphImpl(ZLTextPlainModel model, int index) {
		myModel = model;
		myIndex = index;
	}

	public EntryIterator iterator() {
		return myModel.new EntryIteratorImpl(myIndex);
	}

	public byte getKind() {
		return Kind.TEXT_PARAGRAPH;
	}
}
