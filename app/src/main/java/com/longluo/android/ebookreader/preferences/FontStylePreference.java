package com.longluo.android.ebookreader.preferences;

import android.content.Context;

import com.longluo.zlibrary.core.options.ZLBooleanOption;
import com.longluo.zlibrary.core.resources.ZLResource;

class FontStylePreference extends ZLStringListPreference {
	private final ZLBooleanOption myBoldOption;
	private final ZLBooleanOption myItalicOption;
	private final String[] myValues = { "regular", "bold", "italic", "boldItalic" };

	FontStylePreference(Context context, ZLResource resource, ZLBooleanOption boldOption, ZLBooleanOption italicOption) {
		super(context, resource);

		myBoldOption = boldOption;
		myItalicOption = italicOption;
		setList(myValues);

		final int intValue =
			(boldOption.getValue() ? 1 : 0) |
			(italicOption.getValue() ? 2 : 0);
		setInitialValue(myValues[intValue]);
	}

	@Override
	protected void onDialogClosed(boolean result) {
		super.onDialogClosed(result);
		final int intValue = findIndexOfValue(getValue());
		myBoldOption.setValue((intValue & 0x1) == 0x1);
		myItalicOption.setValue((intValue & 0x2) == 0x2);
	}
}
