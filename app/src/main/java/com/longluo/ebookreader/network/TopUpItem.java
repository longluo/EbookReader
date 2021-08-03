package com.longluo.ebookreader.network;

import com.longluo.ebookreader.network.urlInfo.UrlInfoCollection;

public class TopUpItem extends NetworkItem {
	public TopUpItem(INetworkLink link, UrlInfoCollection<?> urls) {
		super(
			link,
			NetworkLibrary.resource().getResource("topupTitle").getValue(),
			NetworkLibrary.resource().getResource("topupSummary").getValue(),
			urls
		);
	}
}
