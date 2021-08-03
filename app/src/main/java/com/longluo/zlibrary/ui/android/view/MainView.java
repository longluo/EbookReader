package com.longluo.zlibrary.ui.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.longluo.android.ebookreader.FBReaderMainActivity;

public abstract class MainView extends View {
	protected Integer myColorLevel;

	public MainView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MainView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MainView(Context context) {
		super(context);
	}

	public final void setScreenBrightness(int percent) {
		if (percent < 1) {
			percent = 1;
		} else if (percent > 100) {
			percent = 100;
		}

		final Context context = getContext();
		if (!(context instanceof FBReaderMainActivity)) {
			return;
		}

		final float level;
		final Integer oldColorLevel = myColorLevel;
		if (percent >= 25) {
			// 100 => 1f; 25 => .01f
			level = .01f + (percent - 25) * .99f / 75;
			myColorLevel = null;
		} else {
			level = .01f;
			myColorLevel = 0x60 + (0xFF - 0x60) * Math.max(percent, 0) / 25;
		}

		final FBReaderMainActivity activity = (FBReaderMainActivity)context;
		activity.getZLibrary().ScreenBrightnessLevelOption.setValue(percent);
		activity.setScreenBrightnessSystem(level);
		if (oldColorLevel != myColorLevel) {
			updateColorLevel();
			postInvalidate();
		}
	}

	public final int getScreenBrightness() {
		if (myColorLevel != null) {
			return (myColorLevel - 0x60) * 25 / (0xFF - 0x60);
		}

		final Context context = getContext();
		if (!(context instanceof FBReaderMainActivity)) {
			return 50;
		}
		final float level = ((FBReaderMainActivity)context).getScreenBrightnessSystem();
		// level = .01f + (percent - 25) * .99f / 75;
		return 25 + (int)((level - .01f) * 75 / .99f);
	}

	protected abstract void updateColorLevel();
}
