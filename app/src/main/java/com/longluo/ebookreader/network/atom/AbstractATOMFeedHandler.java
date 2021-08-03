package com.longluo.ebookreader.network.atom;

import com.longluo.zlibrary.core.xml.ZLStringMap;

public abstract class AbstractATOMFeedHandler implements ATOMFeedHandler {
	public void processFeedStart() {
	}

	public void processFeedEnd() {
	}

	public boolean processFeedMetadata(ATOMFeedMetadata feed, boolean beforeEntries) {
		return false;
	}

	public boolean processFeedEntry(ATOMEntry entry) {
		return false;
	}

	public ATOMFeedMetadata createFeed(ZLStringMap attributes) {
		return new ATOMFeedMetadata(attributes);
	}

	public ATOMEntry createEntry(ZLStringMap attributes) {
		return new ATOMEntry(attributes);
	}

	public ATOMLink createLink(ZLStringMap attributes) {
		return new ATOMLink(attributes);
	}
}
