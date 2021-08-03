package com.longluo.ebookreader.network.rss;

import java.util.HashSet;

import com.longluo.zlibrary.core.network.ZLNetworkException;
import com.longluo.zlibrary.core.network.ZLNetworkRequest;
import com.longluo.zlibrary.core.util.MimeType;

import com.longluo.ebookreader.network.*;
import com.longluo.ebookreader.network.tree.NetworkItemsLoader;
import com.longluo.ebookreader.network.urlInfo.UrlInfoCollection;

public class RSSCatalogItem extends NetworkURLCatalogItem {
	static class State extends NetworkOperationData {
		public String LastLoadedId;
		public final HashSet<String> LoadedIds = new HashSet<String>();

		public State(RSSNetworkLink link, NetworkItemsLoader loader) {
			super(link, loader);
		}
	}
	private State myLoadingState;

	protected RSSCatalogItem(INetworkLink link, CharSequence title,
			CharSequence summary, UrlInfoCollection<?> urls,
			Accessibility accessibility, int flags) {
		super(link, title, summary, urls, accessibility, flags);
	}

	@Override
	public void loadChildren(NetworkItemsLoader loader) throws ZLNetworkException {

		final RSSNetworkLink rssLink = (RSSNetworkLink)Link;
		myLoadingState = rssLink.createOperationData(loader);

		doLoadChildren(rssLink.createNetworkData(getCatalogUrl(), myLoadingState));
	}

	private void doLoadChildren(ZLNetworkRequest networkRequest) throws ZLNetworkException {
		try {
			super.doLoadChildren(myLoadingState, networkRequest);
		} catch (ZLNetworkException e) {
			myLoadingState = null;
			throw e;
		}
	}

}
