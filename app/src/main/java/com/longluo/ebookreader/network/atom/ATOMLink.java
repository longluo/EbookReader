package com.longluo.ebookreader.network.atom;

import com.longluo.zlibrary.core.xml.ZLStringMap;

public class ATOMLink extends ATOMCommonAttributes {
	public static final String HREF = "href";
	public static final String REL = "rel";
	public static final String TYPE = "type";
	public static final String HREFLANG = "hreflang";
	public static final String TITLE = "title";
	public static final String LENGTH = "length";

	protected ATOMLink(ZLStringMap source) {
		super(source);
		readAttribute(HREF, source);
		readAttribute(REL, source);
		readAttribute(TYPE, source);
		readAttribute(HREFLANG, source);
		readAttribute(TITLE, source);
		readAttribute(LENGTH, source);
	}

	public final String getHref() {
		return getAttribute(HREF);
	}

	public final String getRel() {
		return getAttribute(REL);
	}

	public final String getType() {
		return getAttribute(TYPE);
	}

	public final String getHrefLang() {
		return getAttribute(HREFLANG);
	}

	public final String getTitle() {
		return getAttribute(TITLE);
	}

	public final String getLength() {
		return getAttribute(LENGTH);
	}
}
