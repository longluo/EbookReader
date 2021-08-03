package com.longluo.android.ebookreader;

import com.longluo.ebookreader.fbreader.FBReaderApp;

class StartScreenAction extends FBAndroidAction {
	StartScreenAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	protected void run(Object ... params) {
		Reader.openHelpBook();
	}
}
