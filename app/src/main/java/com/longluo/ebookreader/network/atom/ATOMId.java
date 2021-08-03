package com.longluo.ebookreader.network.atom;

import com.longluo.zlibrary.core.xml.ZLStringMap;

public class ATOMId extends ATOMCommonAttributes {
	public String Uri;

	public ATOMId() {
		this(new ZLStringMap());
	}

	protected ATOMId(ZLStringMap attributes) {
		super(attributes);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ATOMId)) {
			return false;
		}
		ATOMId id = (ATOMId) o;
		return Uri.equals(id.Uri);
	}

	@Override
	public int hashCode() {
		return Uri.hashCode();
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
