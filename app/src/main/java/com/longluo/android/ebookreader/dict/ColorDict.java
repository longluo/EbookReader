package com.longluo.android.ebookreader.dict;

import android.content.Intent;

import com.longluo.android.ebookreader.FBReaderMainActivity;

final class ColorDict extends DictionaryUtil.PackageInfo {
	private interface ColorDict3 {
		String ACTION = "colordict.intent.action.SEARCH";
		String QUERY = "EXTRA_QUERY";
		String HEIGHT = "EXTRA_HEIGHT";
		String WIDTH = "EXTRA_WIDTH";
		String GRAVITY = "EXTRA_GRAVITY";
		String MARGIN_LEFT = "EXTRA_MARGIN_LEFT";
		String MARGIN_TOP = "EXTRA_MARGIN_TOP";
		String MARGIN_BOTTOM = "EXTRA_MARGIN_BOTTOM";
		String MARGIN_RIGHT = "EXTRA_MARGIN_RIGHT";
		String FULLSCREEN = "EXTRA_FULLSCREEN";
	}

	ColorDict(String id, String title) {
		super(id, title);
	}

	@Override
	void open(String text, Runnable outliner, FBReaderMainActivity fbreader, DictionaryUtil.PopupFrameMetric frameMetrics) {
		final Intent intent = getActionIntent(text);
		intent.putExtra(ColorDict3.HEIGHT, frameMetrics.Height);
		intent.putExtra(ColorDict3.GRAVITY, frameMetrics.Gravity);
		intent.putExtra(ColorDict3.FULLSCREEN, !fbreader.getZLibrary().ShowStatusBarOption.getValue());
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		InternalUtil.startDictionaryActivity(fbreader, intent, this);
	}
}
