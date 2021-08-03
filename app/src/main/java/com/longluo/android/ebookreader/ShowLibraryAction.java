package com.longluo.android.ebookreader;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import com.longluo.ebookreader.fbreader.FBReaderApp;

import com.longluo.android.ebookreader.api.FBReaderIntents;
import com.longluo.android.ebookreader.library.LibraryActivity;
import com.longluo.android.util.OrientationUtil;
import com.longluo.android.util.PackageUtil;

class ShowLibraryAction extends FBAndroidAction {
	ShowLibraryAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	protected void run(Object ... params) {
		final Intent externalIntent =
			new Intent(FBReaderIntents.Action.EXTERNAL_LIBRARY);
		final Intent internalIntent =
			new Intent(BaseActivity.getApplicationContext(), LibraryActivity.class);
		if (PackageUtil.canBeStarted(BaseActivity, externalIntent, true)) {
			try {
				startLibraryActivity(externalIntent);
			} catch (ActivityNotFoundException e) {
				startLibraryActivity(internalIntent);
			}
		} else {
			startLibraryActivity(internalIntent);
		}
	}

	private void startLibraryActivity(Intent intent) {
		if (Reader.Model != null) {
			FBReaderIntents.putBookExtra(intent, Reader.getCurrentBook());
		}
		OrientationUtil.startActivity(BaseActivity, intent);
	}
}
