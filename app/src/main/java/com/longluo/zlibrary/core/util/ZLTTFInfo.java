package com.longluo.zlibrary.core.util;

public final class ZLTTFInfo {
	public final String FamilyName;
	public final String SubfamilyName;

	public ZLTTFInfo(String family, String subfamily) {
		FamilyName = family;
		if ("Literata".equals(family) && "Bold Literata".equals(subfamily)) {
			SubfamilyName = "Bold Italic";
		} else {
			SubfamilyName = subfamily;
		}
	}

	@Override
	public String toString() {
		return "FontInfo [" + FamilyName + " (" + SubfamilyName + ")]";
	}
}
