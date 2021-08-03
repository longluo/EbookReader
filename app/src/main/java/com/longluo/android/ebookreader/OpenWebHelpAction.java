package com.longluo.android.ebookreader;

import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.net.Uri;

import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.fbreader.FBReaderApp;

class OpenWebHelpAction extends FBAndroidAction {
	OpenWebHelpAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	protected void run(Object ... params) {
		final String url = ZLResource.resource("links").getResource("faqPage").getValue();
		final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		new Thread(new Runnable() {
			public void run() {
				BaseActivity.runOnUiThread(new Runnable() {
					public void run() {
						try {
							BaseActivity.startActivity(intent);
						} catch (ActivityNotFoundException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}
}
