package com.longluo.android.ebookreader.preferences;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.Preference;

import com.longluo.zlibrary.core.resources.ZLResource;

class UrlPreference extends Preference implements Preference.OnPreferenceClickListener {
	private final String myUrl;

	UrlPreference(Context context, ZLResource resource, String resourceKey) {
		super(context);
		resource = resource.getResource(resourceKey);
		myUrl = resource.getResource("url").getValue();
		setTitle(resource.getValue());
		setSummary(myUrl);
		setOnPreferenceClickListener(this);
	}

	public boolean onPreferenceClick(Preference preference) {
		try {
			getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(myUrl)));
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return true;
	}
}
