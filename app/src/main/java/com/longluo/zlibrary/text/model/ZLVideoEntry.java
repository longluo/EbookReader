package com.longluo.zlibrary.text.model;

import java.util.*;

public class ZLVideoEntry {
	private final Map<String,String> mySources = new HashMap<String,String>();

	public void addSource(String mime, String path) {
		mySources.put(mime, path);
	}

	public Map<String,String> sources() {
		return Collections.unmodifiableMap(mySources);
	}
}
