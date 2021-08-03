package com.longluo.ebookreader.fbreader;

import com.longluo.ebookreader.fbreader.options.PageTurningOptions;

class TurnPageAction extends FBAction {
	private final boolean myForward;

	TurnPageAction(FBReaderApp fbreader, boolean forward) {
		super(fbreader);
		myForward = forward;
	}

	@Override
	public boolean isEnabled() {
		final PageTurningOptions.FingerScrollingType fingerScrolling =
			Reader.PageTurningOptions.FingerScrolling.getValue();
		return
			fingerScrolling == PageTurningOptions.FingerScrollingType.byTap ||
			fingerScrolling == PageTurningOptions.FingerScrollingType.byTapAndFlick;
	}

	@Override
	protected void run(Object ... params) {
		final PageTurningOptions preferences = Reader.PageTurningOptions;
		if (params.length == 2 && params[0] instanceof Integer && params[1] instanceof Integer) {
			final int x = (Integer)params[0];
			final int y = (Integer)params[1];
			Reader.getViewWidget().startAnimatedScrolling(
				myForward ? FBView.PageIndex.next : FBView.PageIndex.previous,
				x, y,
				preferences.Horizontal.getValue()
					? FBView.Direction.rightToLeft : FBView.Direction.up,
				preferences.AnimationSpeed.getValue()
			);
		} else {
			Reader.getViewWidget().startAnimatedScrolling(
				myForward ? FBView.PageIndex.next : FBView.PageIndex.previous,
				preferences.Horizontal.getValue()
					? FBView.Direction.rightToLeft : FBView.Direction.up,
				preferences.AnimationSpeed.getValue()
			);
		}
	}
}
