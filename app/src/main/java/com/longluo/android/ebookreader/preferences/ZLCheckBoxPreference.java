package com.longluo.android.ebookreader.preferences;

import android.content.Context;
import android.preference.CheckBoxPreference;

import com.longluo.zlibrary.core.resources.ZLResource;

public abstract class ZLCheckBoxPreference extends CheckBoxPreference {
	protected final ZLResource Resource;

	protected ZLCheckBoxPreference(Context context, ZLResource resource) {
		super(context);

		Resource = resource;
		setTitle(resource.getValue());
		final ZLResource onResource = resource.getResource("summaryOn");
		if (onResource.hasValue()) {
			setSummaryOn(onResource.getValue());
		}
		final ZLResource offResource = resource.getResource("summaryOff");
		if (offResource.hasValue()) {
			setSummaryOff(offResource.getValue());
		}
	}
}
