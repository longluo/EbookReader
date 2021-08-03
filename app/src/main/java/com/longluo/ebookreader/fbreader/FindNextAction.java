package com.longluo.ebookreader.fbreader;

class FindNextAction extends FBAction {
	FindNextAction(FBReaderApp fbreader) {
		super(fbreader);
	}

	@Override
	public boolean isEnabled() {
		FBView view = Reader.getTextView();
		return (view != null) && view.canFindNext();
	}

	@Override
	protected void run(Object ... params) {
		Reader.getTextView().findNext();
	}
}
