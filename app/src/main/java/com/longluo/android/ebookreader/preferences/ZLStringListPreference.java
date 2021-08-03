package com.longluo.android.ebookreader.preferences;

import android.content.Context;
import android.preference.ListPreference;

import com.longluo.zlibrary.core.resources.ZLResource;

abstract class ZLStringListPreference extends ListPreference {
	protected final ZLResource myValuesResource;

	ZLStringListPreference(Context context, ZLResource resource) {
		this(context, resource, resource);
	}

	ZLStringListPreference(Context context, ZLResource resource, ZLResource valuesResource) {
		super(context);
		setTitle(resource.getValue());
		myValuesResource = valuesResource;

		final ZLResource buttonResource = ZLResource.resource("dialog").getResource("button");
		setPositiveButtonText(buttonResource.getResource("ok").getValue());
		setNegativeButtonText(buttonResource.getResource("cancel").getValue());
	}

	protected final void setList(String[] values) {
		String[] texts = new String[values.length];
		for (int i = 0; i < values.length; ++i) {
			final ZLResource resource = myValuesResource.getResource(values[i]);
			texts[i] = resource.hasValue() ? resource.getValue() : values[i];
		}
		setLists(values, texts);
	}

	protected final void setLists(String[] values, String[] texts) {
		assert(values.length == texts.length);

		setEntryValues(values);

		// It appears that setEntries() DOES NOT perform any formatting on the char sequences
		// http://developer.android.com/reference/android/preference/ListPreference.html#setEntries(java.lang.CharSequence[])
		final String[] entries = new String[texts.length];
		for (int i = 0; i < texts.length; ++i) {
			try {
				entries[i] = String.format(texts[i]);
			} catch (Exception e) {
				entries[i] = texts[i];
			}
		}
		setEntries(entries);
	}

	protected final boolean setInitialValue(String value) {
		int index = 0;
		boolean found = false;
		final CharSequence[] entryValues = getEntryValues();
		if (value != null) {
			for (int i = 0; i < entryValues.length; ++i) {
				if (value.equals(entryValues[i])) {
					index = i;
					found = true;
					break;
				}
			}
		}
		setValueIndex(index);
		return found;
	}

	@Override
	public CharSequence getSummary() {
		// standard getSummary() calls extra String.format(), that causes exceptions in some cases
		return getEntry();
	}
}
