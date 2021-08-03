package com.longluo.ebookreader.network;

import com.longluo.zlibrary.core.network.ZLNetworkException;
import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.network.tree.NetworkItemsLoader;
import com.longluo.ebookreader.network.urlInfo.UrlInfo;
import com.longluo.ebookreader.network.urlInfo.UrlInfoCollection;

public class RecentCatalogListItem extends NetworkCatalogItem {
	RecentCatalogListItem(ZLResource resource) {
		super(
			null,
			resource.getValue(),
			resource.getResource("summary").getValue(),
			new UrlInfoCollection<UrlInfo>(),
			Accessibility.ALWAYS,
			FLAGS_DEFAULT
		);
	}

	@Override
	public String getStringId() {
		return "@RecentCatalogs";
	}

	@Override
	public boolean canBeOpened() {
		// TODO: implement
		return true;
	}

	@Override
	public void loadChildren(NetworkItemsLoader loader) throws ZLNetworkException {
		// TODO: implement
		for (int i = 0; i < 5; ++i) {
			loader.onNewItem(new RecentCatalogItem("Catalog " + i, "Visited ..."));
		}
		loader.Tree.confirmAllItems();
	}
}
