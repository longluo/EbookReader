package com.longluo.zlibrary.text.model;

import java.util.Map;

public class ExtensionEntry {
	public final String Type;
	public final Map<String,String> Data;

	ExtensionEntry(String type, Map<String,String> data) {
		Type = type;
		Data = data;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ExtensionEntry)) {
			return false;
		}
		final ExtensionEntry entry = (ExtensionEntry)other;
		return Type.equals(entry.Type) && Data.equals(entry.Data);
	}

	@Override
	public int hashCode() {
		return Type.hashCode() + 23 * Data.hashCode();
	}
}
