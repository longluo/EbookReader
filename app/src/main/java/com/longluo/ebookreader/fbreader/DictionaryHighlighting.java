package com.longluo.ebookreader.fbreader;

import com.longluo.zlibrary.core.util.ZLColor;
import com.longluo.zlibrary.text.view.*;

public final class DictionaryHighlighting extends ZLTextSimpleHighlighting {
	public static DictionaryHighlighting get(ZLTextView view) {
		final ZLTextHighlighting hilite = view.getSelectionHighlighting();
		if (hilite == null) {
			return null;
		}

		final ZLTextPosition start = hilite.getStartPosition();
		final ZLTextPosition end = hilite.getEndPosition();
		if (start == null || end == null) {
			return null;
		}

		return new DictionaryHighlighting(view, start, end);
	}

	private DictionaryHighlighting(ZLTextView view, ZLTextPosition start, ZLTextPosition end) {
		super(view, start, end);
	}

	@Override
	public ZLColor getBackgroundColor() {
		return View.getSelectionBackgroundColor();
	}

	@Override
	public ZLColor getForegroundColor() {
		return null;
	}

	@Override
	public ZLColor getOutlineColor() {
		return null;
	}
}
