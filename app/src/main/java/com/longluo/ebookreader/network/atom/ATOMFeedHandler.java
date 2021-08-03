package com.longluo.ebookreader.network.atom;

import com.longluo.zlibrary.core.xml.ZLStringMap;

public interface ATOMFeedHandler<MetadataType extends ATOMFeedMetadata,EntryType extends ATOMEntry> {
	void processFeedStart();

	// returns true iff reading process should be interrupted
	boolean processFeedMetadata(MetadataType feed, boolean beforeEntries);

	// returns true iff reading process should be interrupted
	boolean processFeedEntry(EntryType entry);

	void processFeedEnd();

	MetadataType createFeed(ZLStringMap attributes);
	EntryType createEntry(ZLStringMap attributes);
	ATOMLink createLink(ZLStringMap attributes);
}
