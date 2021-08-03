package com.longluo.android.ebookreader.preferences;

import android.content.Context;
import android.preference.EditTextPreference;

import com.longluo.zlibrary.core.resources.ZLResource;

public abstract class ZLStringPreference extends EditTextPreference {
	private String myValue;

	protected ZLStringPreference(Context context, ZLResource rootResource, String resourceKey) {
		super(context);

		ZLResource resource = rootResource.getResource(resourceKey);
		setTitle(resource.getValue());
	}

	protected void setValue(String value) {
		setSummary(value);
		setText(value);
		myValue = value;
	}

	protected final String getValue() {
		return myValue;
	}

	@Override
	protected void onDialogClosed(boolean result) {
		if (result) {
			setValue(getEditText().getText().toString());
		}
		super.onDialogClosed(result);
	}
}
