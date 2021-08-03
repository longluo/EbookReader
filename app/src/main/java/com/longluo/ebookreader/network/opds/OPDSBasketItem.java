package com.longluo.ebookreader.network.opds;

import java.util.List;

import com.longluo.zlibrary.core.network.ZLNetworkContext;
import com.longluo.zlibrary.core.network.ZLNetworkException;
import com.longluo.zlibrary.core.util.MimeType;
import com.longluo.zlibrary.core.util.MiscUtil;

import com.longluo.ebookreader.network.BasketItem;
import com.longluo.ebookreader.network.NetworkLibrary;
import com.longluo.ebookreader.network.urlInfo.*;
import com.longluo.ebookreader.network.tree.NetworkItemsLoader;

class OPDSBasketItem extends BasketItem {
	OPDSBasketItem(NetworkLibrary library, OPDSNetworkLink link) {
		super(library, link);
	}

	@Override
	public void loadChildren(NetworkItemsLoader loader) throws ZLNetworkException {
		final List<String> ids = bookIds();
		if (ids.isEmpty()) {
			return;
		}

		if (isFullyLoaded()) {
			for (String id : ids) {
				loader.onNewItem(getBook(id));
			}
			loader.Tree.confirmAllItems();
			return;
		}

		final OPDSNetworkLink opdsLink = (OPDSNetworkLink)Link;
		String url = opdsLink.getUrl(UrlInfo.Type.ListBooks);
		if (url == null) {
			return;
		}
		url = url.replace("{ids}", MiscUtil.join(ids, ","));

		final OPDSCatalogItem.State state = opdsLink.createOperationData(loader);
		doLoadChildren(state, opdsLink.createNetworkData(url, state));
	}
}
