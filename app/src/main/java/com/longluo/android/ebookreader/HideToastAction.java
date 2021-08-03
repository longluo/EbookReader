package com.longluo.android.ebookreader;

import com.longluo.ebookreader.fbreader.FBReaderApp;

public class HideToastAction extends FBAndroidAction {
	HideToastAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	public boolean isEnabled() {
		return BaseActivity.isToastShown();
	}

	@Override
	protected void run(Object ... params) {
		BaseActivity.hideToast();
	}
}
