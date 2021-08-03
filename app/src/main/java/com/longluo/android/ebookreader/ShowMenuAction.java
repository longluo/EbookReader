package com.longluo.android.ebookreader;

import com.longluo.ebookreader.fbreader.FBReaderApp;

class ShowMenuAction extends FBAndroidAction {
	ShowMenuAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	protected void run(Object ... params) {
		BaseActivity.openOptionsMenu();
	}
}
