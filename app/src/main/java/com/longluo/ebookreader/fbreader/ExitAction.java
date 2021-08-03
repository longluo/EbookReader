package com.longluo.ebookreader.fbreader;

class ExitAction extends FBAction {
	ExitAction(FBReaderApp fbreader) {
		super(fbreader);
	}

	@Override
	protected void run(Object ... params) {
		if (Reader.getCurrentView() != Reader.BookTextView) {
			Reader.showBookTextView();
		} else {
			Reader.closeWindow();
		}
	}
}
