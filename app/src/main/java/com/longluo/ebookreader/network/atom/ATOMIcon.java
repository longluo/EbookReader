package com.longluo.ebookreader.network.atom;

import com.longluo.zlibrary.core.xml.ZLStringMap;

public class ATOMIcon extends ATOMCommonAttributes {
	public String Uri;

	protected ATOMIcon(ZLStringMap attributes) {
		super(attributes);
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder("[");
		buf.append(super.toString());
		buf.append(",\nUri=").append(Uri);
		buf.append("]");
		return buf.toString();
	}
}
