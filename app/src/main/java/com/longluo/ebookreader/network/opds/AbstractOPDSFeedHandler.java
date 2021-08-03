package com.longluo.ebookreader.network.opds;

import com.longluo.zlibrary.core.xml.ZLStringMap;

import com.longluo.ebookreader.network.atom.ATOMFeedHandler;

abstract class AbstractOPDSFeedHandler implements ATOMFeedHandler<OPDSFeedMetadata,OPDSEntry>, OPDSConstants {
	public OPDSFeedMetadata createFeed(ZLStringMap attributes) {
		return new OPDSFeedMetadata(attributes);
	}

	public OPDSEntry createEntry(ZLStringMap attributes) {
		return new OPDSEntry(attributes);
	}

	public OPDSLink createLink(ZLStringMap attributes) {
		return new OPDSLink(attributes);
	}
}
