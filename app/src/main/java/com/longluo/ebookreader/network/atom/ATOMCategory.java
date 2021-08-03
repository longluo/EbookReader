package com.longluo.ebookreader.network.atom;

import com.longluo.zlibrary.core.xml.ZLStringMap;

public class ATOMCategory extends ATOMCommonAttributes {
	public static final String TERM = "term";
	public static final String SCHEME = "scheme";
	public static final String LABEL = "label";

	protected ATOMCategory(ZLStringMap source) {
		super(source);
		readAttribute(TERM, source);
		readAttribute(SCHEME, source);
		readAttribute(LABEL, source);
	}

	public final String getTerm() {
		return getAttribute(TERM);
	}

	public final String getScheme() {
		return getAttribute(SCHEME);
	}

	public final String getLabel() {
		return getAttribute(LABEL);
	}
}
