package com.longluo.android.ebookreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.github.johnpersano.supertoasts.SuperActivityToast;

import com.longluo.zlibrary.core.options.*;
import com.longluo.zlibrary.ui.android.library.*;
import com.longluo.zlibrary.ui.android.view.AndroidFontUtil;

import com.longluo.android.ebookreader.dict.DictionaryUtil;

public abstract class FBReaderMainActivity extends Activity {
	public static final int REQUEST_PREFERENCES = 1;
	public static final int REQUEST_CANCEL_MENU = 2;
	public static final int REQUEST_DICTIONARY = 3;

	private volatile SuperActivityToast myToast;

	@Override
	protected void onCreate(Bundle saved) {
		super.onCreate(saved);
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(this));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			default:
				super.onActivityResult(requestCode, resultCode, data);
				break;
			case REQUEST_DICTIONARY:
				DictionaryUtil.onActivityResult(this, resultCode, data);
				break;
		}
	}

	public ZLAndroidLibrary getZLibrary() {
		return ((ZLAndroidApplication)getApplication()).library();
	}

	/* ++++++ SCREEN BRIGHTNESS ++++++ */
	protected void setScreenBrightnessAuto() {
		final WindowManager.LayoutParams attrs = getWindow().getAttributes();
		attrs.screenBrightness = -1.0f;
		getWindow().setAttributes(attrs);
	}

	public void setScreenBrightnessSystem(float level) {
		final WindowManager.LayoutParams attrs = getWindow().getAttributes();
		attrs.screenBrightness = level;
		getWindow().setAttributes(attrs);
	}

	public float getScreenBrightnessSystem() {
		final float level = getWindow().getAttributes().screenBrightness;
		return level >= 0 ? level : .5f;
	}
	/* ------ SCREEN BRIGHTNESS ------ */

	/* ++++++ SUPER TOAST ++++++ */
	public boolean isToastShown() {
		final SuperActivityToast toast = myToast;
		return toast != null && toast.isShowing();
	}

	public void hideToast() {
		final SuperActivityToast toast = myToast;
		if (toast != null && toast.isShowing()) {
			myToast = null;
			runOnUiThread(new Runnable() {
				public void run() {
					toast.dismiss();
				}
			});
		}
	}

	public void showToast(final SuperActivityToast toast) {
		hideToast();
		myToast = toast;
		// TODO: avoid this hack (accessing text style via option)
		final int dpi = getZLibrary().getDisplayDPI();
		final int defaultFontSize = dpi * 18 / 160;
		final int fontSize = new ZLIntegerOption("Style", "Base:fontSize", defaultFontSize).getValue();
		final int percent = new ZLIntegerRangeOption("Options", "ToastFontSizePercent", 25, 100, 90).getValue();
		final int dpFontSize = fontSize * 160 * percent / dpi / 100;
		toast.setTextSize(dpFontSize);
		toast.setButtonTextSize(dpFontSize * 7 / 8);

		final String fontFamily =
			new ZLStringOption("Style", "Base:fontFamily", "sans-serif").getValue();
		toast.setTypeface(AndroidFontUtil.systemTypeface(fontFamily, false, false));

		runOnUiThread(new Runnable() {
			public void run() {
				toast.show();
			}
		});
	}
	/* ------ SUPER TOAST ------ */

	public abstract void hideDictionarySelection();
}
