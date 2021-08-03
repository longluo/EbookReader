package com.longluo.android.ebookreader;

import com.longluo.ebookreader.fbreader.FBAction;
import com.longluo.ebookreader.fbreader.FBReaderApp;

abstract class FBAndroidAction extends FBAction {
	protected final FBReader BaseActivity;

	FBAndroidAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(fbreader);
		BaseActivity = baseActivity;
	}
}
