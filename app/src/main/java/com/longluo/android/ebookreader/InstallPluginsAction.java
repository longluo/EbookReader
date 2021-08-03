package com.longluo.android.ebookreader;

import android.content.Intent;

import com.longluo.ebookreader.fbreader.FBReaderApp;

class InstallPluginsAction extends FBAndroidAction {
	InstallPluginsAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	protected void run(Object ... params) {
		BaseActivity.startActivity(new Intent(BaseActivity, PluginListActivity.class));
	}
}
