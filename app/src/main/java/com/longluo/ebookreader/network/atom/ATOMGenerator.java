package com.longluo.ebookreader.network.atom;

import com.longluo.zlibrary.core.xml.ZLStringMap;

public class ATOMGenerator extends ATOMCommonAttributes {
	public static final String KEY_URI = "uri";
	public static final String KEY_VERSION = "version";

	public String Text;

	protected ATOMGenerator(ZLStringMap source) {
		super(source);
		readAttribute(KEY_URI, source);
		readAttribute(KEY_VERSION, source);
	}

	public final String getUri() {
		return getAttribute(KEY_URI);
	}

	public final String getVersion() {
		return getAttribute(KEY_VERSION);
	}
}
