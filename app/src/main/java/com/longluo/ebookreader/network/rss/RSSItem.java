package com.longluo.ebookreader.network.rss;

import com.longluo.zlibrary.core.xml.ZLStringMap;

import com.longluo.ebookreader.network.atom.ATOMEntry;

public class RSSItem extends ATOMEntry {
	public String SeriesTitle;
	public float SeriesIndex;

	protected RSSItem(ZLStringMap attributes) {
		super(attributes);
	}
}
