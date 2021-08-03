package com.longluo.ebookreader.fbreader;

import com.longluo.zlibrary.core.util.ZLColor;

import com.longluo.zlibrary.text.view.*;

import com.longluo.ebookreader.book.*;

public final class BookmarkHighlighting extends ZLTextSimpleHighlighting {
	final IBookCollection Collection;
	final Bookmark Bookmark;

	private static ZLTextPosition startPosition(Bookmark bookmark) {
		return new ZLTextFixedPosition(bookmark.getParagraphIndex(), bookmark.getElementIndex(), 0);
	}

	private static ZLTextPosition endPosition(Bookmark bookmark) {
		final ZLTextPosition end = bookmark.getEnd();
		if (end != null) {
			return end;
		}
		// TODO: compute end and save bookmark
		return bookmark;
	}

	BookmarkHighlighting(ZLTextView view, IBookCollection collection, Bookmark bookmark) {
		super(view, startPosition(bookmark), endPosition(bookmark));
		Collection = collection;
		Bookmark = bookmark;
	}

	@Override
	public ZLColor getBackgroundColor() {
		final HighlightingStyle bmStyle = Collection.getHighlightingStyle(Bookmark.getStyleId());
		return bmStyle != null ? bmStyle.getBackgroundColor() : null;
	}

	@Override
	public ZLColor getForegroundColor() {
		final HighlightingStyle bmStyle = Collection.getHighlightingStyle(Bookmark.getStyleId());
		return bmStyle != null ? bmStyle.getForegroundColor() : null;
	}

	@Override
	public ZLColor getOutlineColor() {
		return null;
	}
}
