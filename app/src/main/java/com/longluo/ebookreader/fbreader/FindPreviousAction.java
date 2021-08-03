package com.longluo.ebookreader.fbreader;

import com.longluo.zlibrary.text.view.ZLTextView;

class FindPreviousAction extends FBAction {
	FindPreviousAction(FBReaderApp fbreader) {
		super(fbreader);
	}

	@Override
	public boolean isEnabled() {
		ZLTextView view = Reader.getTextView();
		return (view != null) && view.canFindPrevious();
	}

	@Override
	protected void run(Object ... params) {
		Reader.getTextView().findPrevious();
	}
}
