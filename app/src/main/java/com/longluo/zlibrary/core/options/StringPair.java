package com.longluo.zlibrary.core.options;

final class StringPair {
	final String Group;
	final String Name;

	StringPair(String group, String name) {
		Group = group.intern();
		Name = name.intern();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		try {
			final StringPair pair = (StringPair)other;
			// yes, I'm sure Group & Name are not nulls
			return Group.equals(pair.Group) && Name.equals(pair.Name);
		} catch (ClassCastException e) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Group.hashCode() + 37 * Name.hashCode();
	}
}
