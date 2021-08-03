package com.longluo.ebookreader.network;

import com.longluo.zlibrary.core.network.ZLNetworkException;

import com.longluo.ebookreader.network.tree.NetworkItemsLoader;
import com.longluo.ebookreader.network.urlInfo.UrlInfo;
import com.longluo.ebookreader.network.urlInfo.UrlInfoCollection;

class RecentCatalogItem extends NetworkCatalogItem {
	RecentCatalogItem(String title, String summary) {
		super(
			null,
			title,
			summary,
			new UrlInfoCollection<UrlInfo>(),
			Accessibility.ALWAYS,
			FLAGS_DEFAULT
		);
	}

	@Override
	public String getStringId() {
		// TODO: implement
		return "@RecentCatalog#" + hashCode();
	}

	@Override
	public boolean canBeOpened() {
		// TODO: implement
		return false;
	}

	@Override
	public void loadChildren(NetworkItemsLoader loader) throws ZLNetworkException {
		// TODO: implement
	}
}
