package com.longluo.ebookreader.util;

import com.longluo.zlibrary.text.view.ZLTextFixedPosition;
import com.longluo.zlibrary.text.view.ZLTextPosition;

public class EmptyTextSnippet implements TextSnippet {
	private final ZLTextPosition myPosition;

	public EmptyTextSnippet(ZLTextPosition position) {
		myPosition = new ZLTextFixedPosition(position);
	}

	public ZLTextPosition getStart() {
		return myPosition;
	}

	public ZLTextPosition getEnd() {
		return myPosition;
	}

	public String getText() {
		return "";
	}
}
