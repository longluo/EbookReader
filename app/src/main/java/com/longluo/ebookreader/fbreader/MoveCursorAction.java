package com.longluo.ebookreader.fbreader;

import com.longluo.zlibrary.text.view.ZLTextRegion;
import com.longluo.zlibrary.text.view.ZLTextWordRegionSoul;

class MoveCursorAction extends FBAction {
	private final FBView.Direction myDirection;

	MoveCursorAction(FBReaderApp fbreader, FBView.Direction direction) {
		super(fbreader);
		myDirection = direction;
	}

	@Override
	protected void run(Object ... params) {
		final FBView fbView = Reader.getTextView();
		ZLTextRegion region = fbView.getOutlinedRegion();
		final ZLTextRegion.Filter filter =
			(region != null && region.getSoul() instanceof ZLTextWordRegionSoul)
				|| Reader.MiscOptions.NavigateAllWords.getValue()
					? ZLTextRegion.AnyRegionFilter : ZLTextRegion.ImageOrHyperlinkFilter;
		region = fbView.nextRegion(myDirection, filter);
		if (region != null) {
			fbView.outlineRegion(region);
		} else {
			switch (myDirection) {
				case down:
					fbView.turnPage(true, FBView.ScrollingMode.SCROLL_LINES, 1);
					break;
				case up:
					fbView.turnPage(false, FBView.ScrollingMode.SCROLL_LINES, 1);
					break;
			}
		}

		Reader.getViewWidget().reset();
		Reader.getViewWidget().repaint();
	}
}
