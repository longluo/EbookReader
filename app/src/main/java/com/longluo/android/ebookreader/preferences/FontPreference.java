package com.longluo.android.ebookreader.preferences;

import java.util.ArrayList;

import android.content.Context;

import com.longluo.zlibrary.core.options.ZLStringOption;
import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.zlibrary.ui.android.view.AndroidFontUtil;

class FontPreference extends ZLStringListPreference implements ReloadablePreference {
	private final ZLStringOption myOption;
	private final boolean myIncludeDummyValue;

	private static String UNCHANGED = "inherit";

	FontPreference(Context context, ZLResource resource, ZLStringOption option, boolean includeDummyValue) {
		super(context, resource);

		myOption = option;
		myIncludeDummyValue = includeDummyValue;

		reload();
	}

	public void reload() {
		final ArrayList<String> fonts = new ArrayList<String>();
		AndroidFontUtil.fillFamiliesList(fonts);
		if (myIncludeDummyValue) {
			fonts.add(0, UNCHANGED);
		}
		setList((String[])fonts.toArray(new String[fonts.size()]));

		final String optionValue = myOption.getValue();
		final String initialValue = optionValue.length() > 0 ?
			AndroidFontUtil.realFontFamilyName(optionValue) : UNCHANGED;
		for (String fontName : fonts) {
			if (initialValue.equals(fontName)) {
				setInitialValue(fontName);
				return;
			}
		}
		for (String fontName : fonts) {
			if (initialValue.equals(AndroidFontUtil.realFontFamilyName(fontName))) {
				setInitialValue(fontName);
				return;
			}
		}
	}

	@Override
	protected void onDialogClosed(boolean result) {
		super.onDialogClosed(result);
		final String value = getValue();
		myOption.setValue(UNCHANGED.equals(value) ? "" : value);
	}
}
