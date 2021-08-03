package com.longluo.android.ebookreader.preferences;

import android.content.Context;
import android.preference.ListPreference;

import com.longluo.zlibrary.core.resources.ZLResource;
import com.longluo.zlibrary.core.options.ZLIntegerRangeOption;

class ZLIntegerRangePreference extends ListPreference {
	private final ZLIntegerRangeOption myOption;

	ZLIntegerRangePreference(Context context, ZLResource resource, ZLIntegerRangeOption option) {
		super(context);
		myOption = option;
		setTitle(resource.getValue());
		String[] entries = new String[option.MaxValue - option.MinValue + 1];
		for (int i = 0; i < entries.length; ++i) {
			entries[i] = ((Integer)(i + option.MinValue)).toString();
		}
		setEntries(entries);
		setEntryValues(entries);
		setValueIndex(option.getValue() - option.MinValue);
		setSummary(getValue());
	}

	@Override
	protected void onDialogClosed(boolean result) {
		super.onDialogClosed(result);
		if (result) {
			final String value = getValue();
			setSummary(value);
			myOption.setValue(myOption.MinValue + findIndexOfValue(value));
		}
	}
}
