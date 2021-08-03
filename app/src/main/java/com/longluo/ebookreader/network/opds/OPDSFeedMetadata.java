package com.longluo.ebookreader.network.opds;

import com.longluo.zlibrary.core.xml.ZLStringMap;

import com.longluo.ebookreader.network.atom.*;

class OPDSFeedMetadata extends ATOMFeedMetadata {
	public int OpensearchTotalResults = -1;
	public int OpensearchItemsPerPage;
	public int OpensearchStartIndex = 1;

	public String ViewType;

	protected OPDSFeedMetadata(ZLStringMap attributes) {
		super(attributes);
	}
}
