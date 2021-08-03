package com.longluo.ebookreader.fbreader;

class ClearFindResultsAction extends FBAction {
	ClearFindResultsAction(FBReaderApp fbreader) {
		super(fbreader);
	}

	@Override
	protected void run(Object ... params) {
		Reader.getTextView().clearFindResults();
	}
}
