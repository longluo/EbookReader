package com.longluo.android.ebookreader.preferences;

import android.content.Context;

import com.longluo.zlibrary.core.resources.ZLResource;
import com.longluo.zlibrary.core.options.ZLIntegerRangeOption;

class BatteryLevelToTurnScreenOffPreference extends ZLStringListPreference {
	private final ZLIntegerRangeOption myOption;

	BatteryLevelToTurnScreenOffPreference(Context context, ZLIntegerRangeOption option, ZLResource resource) {
		super(context, resource);
		myOption = option;
		String[] entries = { "0", "25", "50", "100" };
		setList(entries);

		int value = option.getValue();
		if (value <= 0) {
			setInitialValue("0");
		} else if (value <= 25) {
			setInitialValue("25");
		} else if (value <= 50) {
			setInitialValue("50");
		} else {
			setInitialValue("100");
		}
	}

	@Override
	protected void onDialogClosed(boolean result) {
		super.onDialogClosed(result);
		try {
			myOption.setValue(Integer.parseInt(getValue()));
		} catch (NumberFormatException e) {
		}
	}
}
