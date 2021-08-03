package com.longluo.ebookreader.network.atom;

import com.longluo.zlibrary.core.xml.ZLStringMap;

abstract class ATOMPersonConstruct extends ATOMCommonAttributes {
	public String Name;
	public String Uri;
	public String Email;

	protected ATOMPersonConstruct(ZLStringMap attributes) {
		super(attributes);
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder("[");
		buf.append(super.toString());
		buf.append(",\nName=").append(Name);
		buf.append(",\nUri=").append(Uri);
		buf.append(",\nEmail=").append(Email);
		buf.append("]");
		return buf.toString();
	}
}
