package com.longluo.android.ebookreader.preferences;

import android.content.Context;

import com.longluo.zlibrary.core.options.ZLBooleanOption;
import com.longluo.zlibrary.core.resources.ZLResource;

class ZLBooleanPreference extends ZLCheckBoxPreference {
	private final ZLBooleanOption myOption;

	ZLBooleanPreference(Context context, ZLBooleanOption option, ZLResource resource) {
		super(context, resource);
		myOption = option;
		setChecked(option.getValue());
	}

	@Override
	protected void onClick() {
		super.onClick();
		myOption.setValue(isChecked());
	}
}
