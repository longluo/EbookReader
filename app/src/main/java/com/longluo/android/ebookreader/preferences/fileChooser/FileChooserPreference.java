package com.longluo.android.ebookreader.preferences.fileChooser;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;

import com.longluo.zlibrary.core.resources.ZLResource;

abstract class FileChooserPreference extends Preference {
	protected final int myRequestCode;
	protected final ZLResource myResource;
	protected final boolean myChooseWritableDirectoriesOnly;
	protected final Runnable myOnValueSetAction;

	FileChooserPreference(Context context, ZLResource rootResource, String resourceKey, boolean chooseWritableDirectoriesOnly, int requestCode, Runnable onValueSetAction) {
		super(context);

		myRequestCode = requestCode;
		myResource = rootResource.getResource(resourceKey);
		setTitle(myResource.getValue());

		myChooseWritableDirectoriesOnly = chooseWritableDirectoriesOnly;
		myOnValueSetAction = onValueSetAction;
	}

	protected abstract String getStringValue();
	protected abstract void setValueFromIntent(Intent data);
}
