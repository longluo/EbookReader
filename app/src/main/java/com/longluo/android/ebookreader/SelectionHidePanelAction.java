package com.longluo.android.ebookreader;

import com.longluo.ebookreader.fbreader.FBReaderApp;

class SelectionHidePanelAction extends FBAndroidAction {
	SelectionHidePanelAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	protected void run(Object ... params) {
		BaseActivity.hideSelectionPanel();
	}
}
