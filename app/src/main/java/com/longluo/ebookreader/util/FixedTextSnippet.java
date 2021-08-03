package com.longluo.ebookreader.util;

import com.longluo.zlibrary.text.view.ZLTextPosition;

public final class FixedTextSnippet implements TextSnippet {
	private final ZLTextPosition myStart;
	private final ZLTextPosition myEnd;
	private final String myText;

	public FixedTextSnippet(ZLTextPosition start, ZLTextPosition end, String text) {
		myStart = start;
		myEnd = end;
		myText = text;
	}

	public ZLTextPosition getStart() {
		return myStart;
	}

	public ZLTextPosition getEnd() {
		return myEnd;
	}

	public String getText() {
		return myText;
	}
}
