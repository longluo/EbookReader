package com.longluo.android.ebookreader.preferences;

import java.util.*;

import android.content.Context;

import com.longluo.zlibrary.core.language.Language;
import com.longluo.zlibrary.core.resources.ZLResource;

abstract class LanguagePreference extends ZLStringListPreference {
	LanguagePreference(
		Context context, ZLResource resource, List<Language> languages
	) {
		super(context, resource);

		final int size = languages.size();
		String[] codes = new String[size];
		String[] names = new String[size];
		int index = 0;
		for (Language l : languages) {
			codes[index] = l.Code;
			names[index] = l.Name;
			++index;
		}
		setLists(codes, names);
		init();
	}

	@Override
	protected void onDialogClosed(boolean result) {
		super.onDialogClosed(result);
		if (result) {
			setLanguage(getValue());
		}
	}

	protected abstract void init();
	protected abstract void setLanguage(String code);
}
