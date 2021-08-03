package com.longluo.android.ebookreader.preferences;

import android.content.Context;

import com.longluo.zlibrary.core.util.ZLColor;
import com.longluo.zlibrary.core.options.ZLColorOption;
import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.R;
import com.longluo.zlibrary.ui.android.util.ZLAndroidColorUtil;

class ZLColorPreference extends ColorPreference {
	private final ZLColorOption myOption;
	private final String myTitle;

	ZLColorPreference(Context context, ZLResource resource, String resourceKey, ZLColorOption option) {
		super(context);
		myOption = option;
		setWidgetLayoutResource(R.layout.color_preference);

		myTitle = resource.getResource(resourceKey).getValue();
	}

	@Override
	public String getTitle() {
		return myTitle;
	}

	@Override
	protected ZLColor getSavedColor() {
		return myOption.getValue();
	}

	@Override
	protected void saveColor(ZLColor color) {
		myOption.setValue(color);
	}
}
