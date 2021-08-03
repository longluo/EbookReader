package com.longluo.android.ebookreader;

import com.longluo.ebookreader.fbreader.FBReaderApp;

class SearchAction extends FBAndroidAction {
	SearchAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	public boolean isVisible() {
		return Reader.Model != null;
	}

	@Override
	protected void run(Object ... params) {
		BaseActivity.onSearchRequested();
	}
}
