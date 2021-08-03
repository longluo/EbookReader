package com.longluo.ebookreader.fbreader;

import com.longluo.zlibrary.text.view.*;

class TextBuildTraverser extends ZLTextTraverser {
	protected final StringBuilder myBuffer = new StringBuilder();

	TextBuildTraverser(ZLTextView view) {
		super(view);
	}

	@Override
	protected void processWord(ZLTextWord word) {
		myBuffer.append(word.Data, word.Offset, word.Length);
	}

	@Override
	protected void processControlElement(ZLTextControlElement control) {
		// does nothing
	}

	@Override
	protected void processSpace() {
		myBuffer.append(" ");
	}

	@Override
	protected void processNbSpace() {
		myBuffer.append("\240");
	}

	@Override
	protected void processEndOfParagraph() {
		myBuffer.append("\n");
	}

	public String getText() {
		return myBuffer.toString();
	}
}
