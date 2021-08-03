package com.longluo.android.util;

import android.app.Activity;
import android.content.Intent;

public abstract class OrientationUtil {
	private static final String KEY = "fbreader.orientation";

	public static void startActivity(Activity current, Intent intent) {
		current.startActivity(intent.putExtra(KEY, current.getRequestedOrientation()));
	}

	public static void startActivityForResult(Activity current, Intent intent, int requestCode) {
		current.startActivityForResult(intent.putExtra(KEY, current.getRequestedOrientation()), requestCode);
	}

	public static void setOrientation(Activity activity, Intent intent) {
		if (intent == null) {
			return;
		}
		final int orientation = intent.getIntExtra(KEY, Integer.MIN_VALUE);
		if (orientation != Integer.MIN_VALUE) {
			activity.setRequestedOrientation(orientation);
		}
	}

	private OrientationUtil() {
	}
}
