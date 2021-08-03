package com.longluo.android.util;

import android.app.Activity;
import android.widget.Toast;

import com.longluo.zlibrary.core.resources.ZLResource;

public abstract class UIMessageUtil {
	public static void showMessageText(final Activity activity, final String text) {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
			}
		});
	}

	public static void showErrorMessage(Activity activity, String resourceKey) {
		showMessageText(
			activity,
			ZLResource.resource("errorMessage").getResource(resourceKey).getValue()
		);
	}

	public static void showErrorMessage(Activity activity, String resourceKey, String parameter) {
		showMessageText(
			activity,
			ZLResource.resource("errorMessage").getResource(resourceKey).getValue().replace("%s", parameter)
		);
	}
}
