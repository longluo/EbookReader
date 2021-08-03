package com.longluo.android.ebookreader;

import android.app.Activity;
import android.content.pm.ActivityInfo;

import com.longluo.util.Boolean3;

import com.longluo.zlibrary.core.library.ZLibrary;
import com.longluo.ebookreader.fbreader.FBReaderApp;

class SetScreenOrientationAction extends FBAndroidAction {
	static void setOrientation(Activity activity, String optionValue) {
		int orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
		if (ZLibrary.SCREEN_ORIENTATION_SENSOR.equals(optionValue)) {
			orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
		} else if (ZLibrary.SCREEN_ORIENTATION_PORTRAIT.equals(optionValue)) {
			orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		} else if (ZLibrary.SCREEN_ORIENTATION_LANDSCAPE.equals(optionValue)) {
			orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		} else if (ZLibrary.SCREEN_ORIENTATION_REVERSE_PORTRAIT.equals(optionValue)) {
			orientation = 9; // ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
		} else if (ZLibrary.SCREEN_ORIENTATION_REVERSE_LANDSCAPE.equals(optionValue)) {
			orientation = 8; // ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
		}
		activity.setRequestedOrientation(orientation);
	}

	private final String myOptionValue;

	SetScreenOrientationAction(FBReader baseActivity, FBReaderApp fbreader, String optionValue) {
		super(baseActivity, fbreader);
		myOptionValue = optionValue;
	}

	@Override
	public Boolean3 isChecked() {
		return myOptionValue.equals(ZLibrary.Instance().getOrientationOption().getValue())
			? Boolean3.TRUE : Boolean3.FALSE;
	}

	@Override
	protected void run(Object ... params) {
		setOrientation(BaseActivity, myOptionValue);
		ZLibrary.Instance().getOrientationOption().setValue(myOptionValue);
		Reader.onRepaintFinished();
	}
}
