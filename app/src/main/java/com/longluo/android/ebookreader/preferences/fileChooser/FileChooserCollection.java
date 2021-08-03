package com.longluo.android.ebookreader.preferences.fileChooser;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;

import com.longluo.zlibrary.core.options.ZLStringOption;
import com.longluo.zlibrary.core.options.ZLStringListOption;
import com.longluo.zlibrary.core.resources.ZLResource;

public class FileChooserCollection {
	private final Context myContext;
	private final int myBaseRequestCode;
	private final List<FileChooserPreference> myPreferences = new ArrayList<FileChooserPreference>();

	public FileChooserCollection(Context context, int baseRequestCode) {
		myContext = context;
		myBaseRequestCode = baseRequestCode;
	}

	public FileChooserPreference createPreference(ZLResource rootResource, String resourceKey, ZLStringListOption option, Runnable onValueSetAction) {
		final FileChooserPreference preference = new FileChooserMultiPreference(
			myContext, rootResource, resourceKey, option, myBaseRequestCode + myPreferences.size(), onValueSetAction
		);
		myPreferences.add(preference);
		return preference;
	}

	public FileChooserPreference createPreference(ZLResource rootResource, String resourceKey, ZLStringOption option, Runnable onValueSetAction) {
		final FileChooserPreference preference = new FileChooserSinglePreference(
			myContext, rootResource, resourceKey, option, myBaseRequestCode + myPreferences.size(), onValueSetAction
		);
		myPreferences.add(preference);
		return preference;
	}

	public void update(int requestCode, Intent data) {
		try {
			myPreferences.get(requestCode - myBaseRequestCode).setValueFromIntent(data);
		} catch (Exception e) {
			// ignore
		}
	}
}
