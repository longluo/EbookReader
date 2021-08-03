package com.longluo.android.ebookreader.preferences;

import android.content.Context;
import android.preference.Preference;

class InfoPreference extends Preference {
	InfoPreference(Context context, String title, String value) {
		super(context);
		setTitle(title);
		setSummary(value);
		setEnabled(false);
	}
}
