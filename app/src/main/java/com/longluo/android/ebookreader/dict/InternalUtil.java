package com.longluo.android.ebookreader.dict;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.view.View;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.OnDismissWrapper;

import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.android.ebookreader.FBReaderMainActivity;
import com.longluo.android.util.PackageUtil;

abstract class InternalUtil {
	static void installDictionaryIfNotInstalled(final Activity activity, final DictionaryUtil.PackageInfo info) {
		if (PackageUtil.canBeStarted(activity, info.getActionIntent("test"), false)) {
			return;
		}

		final Intent intent = new Intent(activity, DictionaryNotInstalledActivity.class);
		intent.putExtra(DictionaryNotInstalledActivity.DICTIONARY_NAME_KEY, info.getTitle());
		intent.putExtra(DictionaryNotInstalledActivity.PACKAGE_NAME_KEY, info.get("package"));
		activity.startActivity(intent);
	}

	static void startDictionaryActivity(FBReaderMainActivity fbreader, Intent intent, DictionaryUtil.PackageInfo info) {
		try {
			fbreader.startActivity(intent);
			fbreader.overridePendingTransition(0, 0);
		} catch (ActivityNotFoundException e) {
			installDictionaryIfNotInstalled(fbreader, info);
		}
	}

	static void showToast(SuperActivityToast toast, final FBReaderMainActivity fbreader) {
		if (toast == null) {
			fbreader.hideDictionarySelection();
			return;
		}

		toast.setOnDismissWrapper(new OnDismissWrapper("dict", new SuperToast.OnDismissListener() {
			@Override
			public void onDismiss(View view) {
				fbreader.hideDictionarySelection();
			}
		}));
		fbreader.showToast(toast);
	}
}
