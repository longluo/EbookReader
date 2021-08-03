package com.longluo.ebookreader.util;

import com.longluo.zlibrary.text.view.ZLTextPosition;

public interface TextSnippet {
	ZLTextPosition getStart();
	ZLTextPosition getEnd();
	String getText();
}
