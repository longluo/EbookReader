package com.longluo.ebookreader.network;

import com.longluo.zlibrary.core.network.ZLNetworkContext;
import com.longluo.zlibrary.core.network.ZLNetworkException;
import com.longluo.zlibrary.core.util.MimeType;

import com.longluo.ebookreader.network.tree.NetworkItemsLoader;
import com.longluo.ebookreader.network.urlInfo.*;

public abstract class SearchItem extends NetworkCatalogItem {
	private String myPattern;

	protected SearchItem(INetworkLink link, String summary) {
		super(
			link,
			NetworkLibrary.resource().getResource("search").getValue(),
			summary,
			new UrlInfoCollection<UrlInfo>(),
			Accessibility.ALWAYS,
			FLAGS_DEFAULT
		);
	}

	public void setPattern(String pattern) {
		myPattern = pattern;
	}

	public String getPattern() {
		return myPattern;
	}

	@Override
	public boolean canBeOpened() {
		return myPattern != null;
	}

	@Override
	public void loadChildren(NetworkItemsLoader loader) throws ZLNetworkException {
	}

	public abstract void runSearch(ZLNetworkContext context, NetworkItemsLoader loader, String pattern) throws ZLNetworkException;

	@Override
	public String getStringId() {
		return "@Search";
	}

	public abstract MimeType getMimeType();
	public abstract String getUrl(String pattern);
}
