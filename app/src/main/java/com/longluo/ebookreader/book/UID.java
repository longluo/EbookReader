package com.longluo.ebookreader.book;

public class UID {
	public final String Type;
	public final String Id;

	public UID(String type, String id) {
		Type = type;
		Id = id.trim();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof UID)) {
			return false;
		}
		final UID u = (UID)o;
		return Type.equals(u.Type) && Id.equals(u.Id);
	}

	@Override
	public int hashCode() {
		return Type.hashCode() + Id.hashCode();
	}
}
