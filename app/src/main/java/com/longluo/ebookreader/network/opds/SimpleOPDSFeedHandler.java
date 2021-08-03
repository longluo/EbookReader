package com.longluo.ebookreader.network.opds;

import java.util.*;

import com.longluo.ebookreader.network.NetworkItem;
import com.longluo.ebookreader.network.NetworkLibrary;

public class SimpleOPDSFeedHandler extends AbstractOPDSFeedHandler implements OPDSConstants {
	private final NetworkLibrary myLibrary;
	private final String myBaseURL;
	private final List<OPDSBookItem> myBooks = new LinkedList<OPDSBookItem>();

	private int myIndex;

	public SimpleOPDSFeedHandler(NetworkLibrary library, String baseURL) {
		myLibrary = library;
		myBaseURL = baseURL;
	}

	@Override
	public void processFeedStart() {
	}

	@Override
	public boolean processFeedMetadata(OPDSFeedMetadata feed, boolean beforeEntries) {
		return false;
	}

	@Override
	public void processFeedEnd() {
	}

	@Override
	public boolean processFeedEntry(OPDSEntry entry) {
		final OPDSBookItem item = new OPDSBookItem(myLibrary, null, entry, myBaseURL, myIndex++);
		for (String identifier : entry.DCIdentifiers) {
			((OPDSBookItem)item).Identifiers.add(identifier);
		}
		myBooks.add(item);
		return false;
	}

	public List<OPDSBookItem> books() {
		return Collections.unmodifiableList(myBooks);
	}
}
