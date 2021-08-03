package com.longluo.android.ebookreader;

import android.content.Intent;

import com.longluo.ebookreader.fbreader.FBReaderApp;

import com.longluo.android.ebookreader.preferences.PreferenceActivity;
import com.longluo.android.util.OrientationUtil;

class ShowPreferencesAction extends FBAndroidAction {
	ShowPreferencesAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	protected void run(Object ... params) {
		final Intent intent =
			new Intent(BaseActivity.getApplicationContext(), PreferenceActivity.class);
		if (params.length == 1 && params[0] instanceof String) {
			intent.putExtra(PreferenceActivity.SCREEN_KEY, (String)params[0]);
		}
		OrientationUtil.startActivityForResult(BaseActivity, intent, FBReader.REQUEST_PREFERENCES);
	}
}
