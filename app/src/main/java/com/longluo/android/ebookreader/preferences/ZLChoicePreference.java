package com.longluo.android.ebookreader.preferences;

import android.content.Context;

import com.longluo.zlibrary.core.options.ZLIntegerRangeOption;
import com.longluo.zlibrary.core.resources.ZLResource;

class ZLChoicePreference extends ZLStringListPreference {
	private final ZLIntegerRangeOption myOption;

	ZLChoicePreference(Context context, ZLResource resource, ZLIntegerRangeOption option, String[] valueResourceKeys) {
		super(context, resource);

		assert(option.MaxValue - option.MinValue + 1 == valueResourceKeys.length);

		myOption = option;
		setList(valueResourceKeys);

		setInitialValue(valueResourceKeys[option.getValue() - option.MinValue]);
	}

	@Override
	protected void onDialogClosed(boolean result) {
		super.onDialogClosed(result);
		myOption.setValue(myOption.MinValue + findIndexOfValue(getValue()));
	}
}
