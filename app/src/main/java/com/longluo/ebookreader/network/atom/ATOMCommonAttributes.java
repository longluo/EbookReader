package com.longluo.ebookreader.network.atom;

import com.longluo.zlibrary.core.xml.ZLStringMap;

abstract class ATOMCommonAttributes {
	public static final String XML_BASE = "xml:base";
	public static final String XML_LANG = "xml:lang";

	private ZLStringMap myAttributes;

	protected ATOMCommonAttributes(ZLStringMap source) {
		readAttribute(XML_BASE, source);
		readAttribute(XML_LANG, source);
	}

	protected final void readAttribute(String name, ZLStringMap source) {
		String value = source.getValue(name);
		if (value != null) {
			value = value.trim().intern();
			if (value.length() > 0) {
				if (myAttributes == null) {
					myAttributes = new ZLStringMap();
				}
				myAttributes.put(name, value);
			}
		}
	}

	public final String getAttribute(String name) {
		if (myAttributes == null) {
			return null;
		}
		return myAttributes.getValue(name);
	}

	public final String getLang() {
		return getAttribute(XML_LANG);
	}

	public final String getBase() {
		return getAttribute(XML_BASE);
	}

	// FIXME: HACK: addAttribute is used ONLY to add OPDSPrice to the ATOMLink... Must be killed + SEE NetworkOPDSFeedReader
	// name and value MUST BE not null AND MUST BE INTERNED String objects
	public final void addAttribute(String name, String value) {
		if (value != null) {
			value = value.trim().intern();
			if (value.length() > 0) {
				if (myAttributes == null) {
					myAttributes = new ZLStringMap();
				}
				myAttributes.put(name, value);
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder("[Attributes:\n");
		if (myAttributes != null) {
			for (int i = 0; i < myAttributes.getSize(); ++i) {
				String key = myAttributes.getKey(i);
				String value = myAttributes.getValue(key);
				if (i != 0) {
					buf.append(",\n");
				}
				buf.append(key).append("=").append(value);
			}
		}
		buf.append("]");
		return buf.toString();
	}
}
