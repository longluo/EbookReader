package com.longluo.zlibrary.text.view;


import androidx.collection.LruCache;

import com.longluo.zlibrary.text.model.ZLTextModel;

final class CursorManager extends LruCache<Integer,ZLTextParagraphCursor> {
	private final ZLTextModel myModel;
	final ExtensionElementManager ExtensionManager;

	CursorManager(ZLTextModel model, ExtensionElementManager extManager) {
		super(200); // max 200 cursors in the cache
		myModel = model;
		ExtensionManager = extManager;
	}

	@Override
	protected ZLTextParagraphCursor create(Integer index) {
		return new ZLTextParagraphCursor(this, myModel, index);
	}
}
