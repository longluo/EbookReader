package com.longluo.ebookreader.network.rss;

import com.longluo.zlibrary.core.xml.ZLStringMap;

import com.longluo.ebookreader.network.atom.ATOMFeedHandler;

public abstract class AbstractRSSChannelHandler implements ATOMFeedHandler<RSSChannelMetadata,RSSItem> {
	public RSSChannelMetadata createFeed(ZLStringMap attributes) {
		return new RSSChannelMetadata(attributes);
	}

	public RSSItem createEntry(ZLStringMap attributes) {
		return new RSSItem(attributes);
	}

	public RSSLink createLink(ZLStringMap attributes) {
		return new RSSLink(attributes);
	}
}
