package com.longluo.zlibrary.text.view;

import com.longluo.zlibrary.core.view.HorizontalConvexHull;

public abstract class ZLTextSimpleHighlighting extends ZLTextHighlighting {
	protected final ZLTextView View;
	private final ZLTextPosition myStartPosition;
	private final ZLTextPosition myEndPosition;

	protected ZLTextSimpleHighlighting(ZLTextView view, ZLTextPosition start, ZLTextPosition end) {
		View = view;
		myStartPosition = new ZLTextFixedPosition(start);
		myEndPosition = new ZLTextFixedPosition(end);
	}

	@Override
	public final boolean isEmpty() {
		return false;
	}

	@Override
	public final ZLTextPosition getStartPosition() {
		return myStartPosition;
	}

	@Override
	public final ZLTextPosition getEndPosition() {
		return myEndPosition;
	}

	@Override
	public final ZLTextElementArea getStartArea(ZLTextPage page) {
		return page.TextElementMap.getFirstAfter(myStartPosition);
	}

	@Override
	public final ZLTextElementArea getEndArea(ZLTextPage page) {
		return page.TextElementMap.getLastBefore(myEndPosition);
	}
}
