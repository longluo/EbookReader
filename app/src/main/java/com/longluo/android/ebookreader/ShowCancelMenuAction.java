package com.longluo.android.ebookreader;

import com.longluo.ebookreader.fbreader.FBReaderApp;

import com.longluo.android.ebookreader.api.FBReaderIntents;

class ShowCancelMenuAction extends FBAndroidAction {
	ShowCancelMenuAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	protected void run(Object ... params) {
		if (!Reader.jumpBack()) {
			if (Reader.hasCancelActions()) {
				BaseActivity.startActivityForResult(
					FBReaderIntents.defaultInternalIntent(FBReaderIntents.Action.CANCEL_MENU),
					FBReader.REQUEST_CANCEL_MENU
				);
			} else {
				Reader.closeWindow();
			}
		}
	}
}
