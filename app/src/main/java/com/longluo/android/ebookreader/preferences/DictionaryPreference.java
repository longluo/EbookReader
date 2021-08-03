package com.longluo.android.ebookreader.preferences;

import java.util.List;

import android.content.Context;

import com.longluo.zlibrary.core.options.ZLStringOption;
import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.android.ebookreader.dict.DictionaryUtil;

class DictionaryPreference extends ZLStringListPreference {
	private final ZLStringOption myOption;

	DictionaryPreference(Context context, ZLResource resource, ZLStringOption dictionaryOption, List<DictionaryUtil.PackageInfo> infos) {
		super(context, resource);

		myOption = dictionaryOption;

		final String[] values = new String[infos.size()];
		final String[] texts = new String[infos.size()];
		int index = 0;
		for (DictionaryUtil.PackageInfo i : infos) {
			values[index] = i.getId();
			texts[index] = i.getTitle();
			++index;
		}
		setLists(values, texts);

		setInitialValue(myOption.getValue());
	}

	@Override
	protected void onDialogClosed(boolean result) {
		super.onDialogClosed(result);
		myOption.setValue(getValue());
	}
}
