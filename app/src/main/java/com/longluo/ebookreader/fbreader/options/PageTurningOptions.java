package com.longluo.ebookreader.fbreader.options;

import com.longluo.zlibrary.core.options.*;
import com.longluo.zlibrary.core.view.ZLView;

public class PageTurningOptions {
	public static enum FingerScrollingType {
		byTap, byFlick, byTapAndFlick
	}
	public final ZLEnumOption<FingerScrollingType> FingerScrolling =
		new ZLEnumOption<FingerScrollingType>("Scrolling", "Finger", FingerScrollingType.byTapAndFlick);

	public final ZLEnumOption<ZLView.Animation> Animation =
		new ZLEnumOption<ZLView.Animation>("Scrolling", "Animation", ZLView.Animation.slide);
	public final ZLIntegerRangeOption AnimationSpeed =
		new ZLIntegerRangeOption("Scrolling", "AnimationSpeed", 1, 10, 7);

	public final ZLBooleanOption Horizontal =
		new ZLBooleanOption("Scrolling", "Horizontal", true);
	public final ZLStringOption TapZoneMap =
		new ZLStringOption("Scrolling", "TapZoneMap", "");
}
