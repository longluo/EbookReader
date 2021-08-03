package com.longluo.android.ebookreader.preferences;

import android.content.Context;

import com.longluo.zlibrary.core.options.ZLStringOption;
import com.longluo.zlibrary.core.resources.ZLResource;

class ZLStringChoicePreference extends ZLStringListPreference {
	private final ZLStringOption myOption;

	ZLStringChoicePreference(Context context, ZLResource resource, ZLStringOption option, String[] values) {
		super(context, resource);
		setList(values);
		setInitialValue(option.getValue());
		myOption = option;
	}

	@Override
	protected void onDialogClosed(boolean result) {
		super.onDialogClosed(result);
		myOption.setValue(getValue());
	}
}
