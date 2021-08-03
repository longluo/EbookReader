package com.longluo.android.ebookreader.preferences.fileChooser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.longluo.zlibrary.core.options.ZLStringOption;
import com.longluo.zlibrary.core.resources.ZLResource;
import com.longluo.zlibrary.core.util.MiscUtil;

import com.longluo.android.util.FileChooserUtil;

class FileChooserSinglePreference extends FileChooserPreference {
	private final ZLStringOption myOption;

	FileChooserSinglePreference(Context context, ZLResource rootResource, String resourceKey, ZLStringOption option, int requestCode, Runnable onValueSetAction) {
		super(context, rootResource, resourceKey, true, requestCode, onValueSetAction);
		myOption = option;

		setSummary(getStringValue());
	}

	@Override
	protected void onClick() {
		FileChooserUtil.runDirectoryChooser(
			(Activity)getContext(),
			myRequestCode,
			myResource.getResource("chooserTitle").getValue(),
			getStringValue(),
			myChooseWritableDirectoriesOnly
		);
	}

	@Override
	protected String getStringValue() {
		return myOption.getValue();
	}

	@Override
	protected void setValueFromIntent(Intent data) {
		final String value = FileChooserUtil.folderPathFromData(data);
		if (MiscUtil.isEmptyString(value)) {
			return;
		}

		final String currentValue = myOption.getValue();
		if (!currentValue.equals(value)) {
			myOption.setValue(value);
			setSummary(value);
		}

		if (myOnValueSetAction != null) {
			myOnValueSetAction.run();
		}
	}
}
