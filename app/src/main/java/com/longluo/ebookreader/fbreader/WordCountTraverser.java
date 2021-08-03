package com.longluo.ebookreader.fbreader;

import com.longluo.zlibrary.text.view.*;

class WordCountTraverser extends ZLTextTraverser {
	protected int myCount;

	WordCountTraverser(ZLTextView view) {
		super(view);
	}

	@Override
	protected void processWord(ZLTextWord word) {
		++myCount;
	}

	@Override
	protected void processControlElement(ZLTextControlElement control) {
		// does nothing
	}

	@Override
	protected void processSpace() {
		// does nothing
	}

	@Override
	protected void processNbSpace() {
		// does nothing
	}

	@Override
	protected void processEndOfParagraph() {
		// does nothing
	}

	public int getCount() {
		return myCount;
	}
}
