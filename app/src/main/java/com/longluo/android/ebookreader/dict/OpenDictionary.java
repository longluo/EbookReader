package com.longluo.android.ebookreader.dict;

import java.util.*;

import android.content.Context;

import com.paragon.dictionary.ebookreader.OpenDictionaryFlyout;
import com.paragon.open.dictionary.api.Dictionary;
import com.paragon.open.dictionary.api.OpenDictionaryAPI;

import com.longluo.android.ebookreader.FBReaderMainActivity;

final class OpenDictionary extends DictionaryUtil.PackageInfo {
	static void collect(Context context, Map<DictionaryUtil.PackageInfo,Integer> dictMap) {
		final SortedSet<Dictionary> dictionariesTreeSet =
			new TreeSet<Dictionary>(new Comparator<Dictionary>() {
				@Override
				public int compare(Dictionary lhs, Dictionary rhs) {
					return lhs.toString().compareTo(rhs.toString());
				}
			}
		);
		dictionariesTreeSet.addAll(
			new OpenDictionaryAPI(context).getDictionaries()
		);

		for (Dictionary dict : dictionariesTreeSet) {
			dictMap.put(new OpenDictionary(dict), DictionaryUtil.FLAG_SHOW_AS_DICTIONARY);
		}
	}

	final OpenDictionaryFlyout Flyout;

	OpenDictionary(Dictionary dictionary) {
		super(dictionary.getUID(), dictionary.getName());
		put("package", dictionary.getApplicationPackageName());
		put("class", ".Start");
		Flyout = new OpenDictionaryFlyout(dictionary);
	}

	@Override
	void open(String text, Runnable outliner, FBReaderMainActivity fbreader, DictionaryUtil.PopupFrameMetric frameMetrics) {
		Flyout.showTranslation(fbreader, text, frameMetrics);
	}
}
