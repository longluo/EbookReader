package com.longluo.ebookreader.fbreader;

public class SelectionClearAction extends FBAction {
	SelectionClearAction(FBReaderApp fbreader) {
		super(fbreader);
	}

	@Override
	protected void run(Object ... params) {
		Reader.getTextView().clearSelection();
	}
}
