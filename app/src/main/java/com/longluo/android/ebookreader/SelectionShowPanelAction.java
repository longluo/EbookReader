package com.longluo.android.ebookreader;

import com.longluo.ebookreader.fbreader.FBReaderApp;

class SelectionShowPanelAction extends FBAndroidAction {
	SelectionShowPanelAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	public boolean isEnabled() {
		return !Reader.getTextView().isSelectionEmpty();
	}

	@Override
	protected void run(Object ... params) {
		BaseActivity.showSelectionPanel();
	}
}
