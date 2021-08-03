package com.longluo.android.ebookreader.preferences.fileChooser;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.longluo.zlibrary.core.options.ZLStringListOption;
import com.longluo.zlibrary.core.resources.ZLResource;
import com.longluo.zlibrary.core.util.MiscUtil;

import com.longluo.android.util.FileChooserUtil;

class FileChooserMultiPreference extends FileChooserPreference {
	private final ZLStringListOption myOption;

	FileChooserMultiPreference(Context context, ZLResource rootResource, String resourceKey, ZLStringListOption option, int requestCode, Runnable onValueSetAction) {
		super(context, rootResource, resourceKey, false, requestCode, onValueSetAction);

		myOption = option;

		setSummary(getStringValue());
	}

	@Override
	protected void onClick() {
		FileChooserUtil.runFolderListDialog(
			(Activity)getContext(),
			myRequestCode,
			myResource.getValue(),
			myResource.getResource("chooserTitle").getValue(),
			myOption.getValue(),
			myChooseWritableDirectoriesOnly
		);
	}

	@Override
	protected String getStringValue() {
		return MiscUtil.join(myOption.getValue(), ", ");
	}

	@Override
	protected void setValueFromIntent(Intent data) {
		final List<String> value = FileChooserUtil.pathListFromData(data);
		if (value.isEmpty()) {
			return;
		}

		myOption.setValue(value);
		setSummary(getStringValue());

		if (myOnValueSetAction != null) {
			myOnValueSetAction.run();
		}
	}
}
