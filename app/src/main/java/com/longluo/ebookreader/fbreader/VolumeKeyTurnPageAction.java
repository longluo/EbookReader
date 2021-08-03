package com.longluo.ebookreader.fbreader;

import com.longluo.ebookreader.fbreader.options.PageTurningOptions;

class VolumeKeyTurnPageAction extends FBAction {
	private final boolean myForward;

	VolumeKeyTurnPageAction(FBReaderApp fbreader, boolean forward) {
		super(fbreader);
		myForward = forward;
	}

	@Override
	protected void run(Object ... params) {
		final PageTurningOptions preferences = Reader.PageTurningOptions;
		Reader.getViewWidget().startAnimatedScrolling(
			myForward ? FBView.PageIndex.next : FBView.PageIndex.previous,
			preferences.Horizontal.getValue()
				? FBView.Direction.rightToLeft : FBView.Direction.up,
			preferences.AnimationSpeed.getValue()
		);
	}
}
