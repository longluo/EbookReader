package com.longluo.zlibrary.core.encodings;

import java.nio.charset.Charset;

public final class JavaEncodingCollection extends FilteredEncodingCollection {
	private volatile static JavaEncodingCollection ourInstance;

	public static JavaEncodingCollection Instance() {
		if (ourInstance == null) {
			ourInstance = new JavaEncodingCollection();
		}
		return ourInstance;
	}

	private JavaEncodingCollection() {
		super();
	}

	@Override
	public boolean isEncodingSupported(String name) {
		try {
			return Charset.forName(name) != null;
		} catch (Exception e) {
			return false;
		}
	}
}
