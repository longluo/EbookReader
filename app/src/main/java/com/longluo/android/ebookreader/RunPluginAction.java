package com.longluo.android.ebookreader;

import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.net.Uri;

import com.longluo.ebookreader.fbreader.FBReaderApp;

import com.longluo.android.ebookreader.api.PluginApi;
import com.longluo.android.util.OrientationUtil;

class RunPluginAction extends FBAndroidAction {
	private final Uri myUri;

	RunPluginAction(FBReader baseActivity, FBReaderApp fbreader, Uri uri) {
		super(baseActivity, fbreader);
		myUri = uri;
	}

	@Override
	protected void run(Object ... params) {
		if (myUri == null) {
			return;
		}
		try {
			OrientationUtil.startActivity(
				BaseActivity, new Intent(PluginApi.ACTION_RUN, myUri)
			);
		} catch (ActivityNotFoundException e) {
		}
	}
}
