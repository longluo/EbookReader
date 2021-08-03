package com.longluo.android.ebookreader;

import android.content.Intent;

import com.longluo.ebookreader.fbreader.FBReaderApp;

import com.longluo.android.ebookreader.network.NetworkLibraryPrimaryActivity;
import com.longluo.android.util.OrientationUtil;

class ShowNetworkLibraryAction extends FBAndroidAction {
	ShowNetworkLibraryAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	protected void run(Object ... params) {
		OrientationUtil.startActivity(
			BaseActivity, new Intent(
				BaseActivity.getApplicationContext(),
				NetworkLibraryPrimaryActivity.class
			)
		);
	}
}
