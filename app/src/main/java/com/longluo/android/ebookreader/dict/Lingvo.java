package com.longluo.android.ebookreader.dict;

import android.content.Intent;

import com.abbyy.mobile.lingvo.api.MinicardContract;

import com.longluo.zlibrary.core.language.Language;

import com.longluo.android.ebookreader.FBReaderMainActivity;

final class Lingvo extends DictionaryUtil.PackageInfo {
	Lingvo(String id, String title) {
		super(id, title, true);
	}

	@Override
	void open(String text, Runnable outliner, FBReaderMainActivity fbreader, DictionaryUtil.PopupFrameMetric frameMetrics) {
		final Intent intent = getActionIntent(text);
		intent.putExtra(MinicardContract.EXTRA_GRAVITY, frameMetrics.Gravity);
		intent.putExtra(MinicardContract.EXTRA_HEIGHT, frameMetrics.Height);
		intent.putExtra(MinicardContract.EXTRA_FORCE_LEMMATIZATION, true);
		intent.putExtra(MinicardContract.EXTRA_TRANSLATE_VARIANTS, true);
		intent.putExtra(MinicardContract.EXTRA_LIGHT_THEME, true);
		final String targetLanguage = DictionaryUtil.TargetLanguageOption.getValue();
		if (!Language.ANY_CODE.equals(targetLanguage)) {
			intent.putExtra(MinicardContract.EXTRA_LANGUAGE_TO, targetLanguage);
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		InternalUtil.startDictionaryActivity(fbreader, intent, this);
	}
}
