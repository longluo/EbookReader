package com.longluo.android.ebookreader.preferences;

import android.content.Context;

import com.longluo.zlibrary.core.options.ZLEnumOption;
import com.longluo.zlibrary.core.resources.ZLResource;

class ZLEnumPreference<T extends Enum<T>> extends ZLStringListPreference {
	private final ZLEnumOption<T> myOption;

	ZLEnumPreference(Context context, ZLEnumOption<T> option, ZLResource resource) {
		this(context, option, resource, resource);
	}

	ZLEnumPreference(Context context, ZLEnumOption<T> option, ZLResource resource, ZLResource valuesResource) {
		super(context, resource, valuesResource);
		myOption = option;

		final T initialValue = option.getValue();
		final T[] allValues = initialValue.getDeclaringClass().getEnumConstants();
		final String[] stringValues = new String[allValues.length];
		for (int i = 0; i < stringValues.length; ++i) {
			stringValues[i] = allValues[i].toString();
		}
		setList(stringValues);
		setInitialValue(initialValue.toString());
	}

	@Override
	protected void onDialogClosed(boolean result) {
		super.onDialogClosed(result);
		myOption.setValue(Enum.valueOf(myOption.getValue().getDeclaringClass(), getValue()));
	}
}
