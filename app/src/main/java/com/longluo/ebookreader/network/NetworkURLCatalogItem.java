package com.longluo.ebookreader.network;

import com.longluo.ebookreader.network.urlInfo.UrlInfo;
import com.longluo.ebookreader.network.urlInfo.UrlInfoCollection;

public abstract class NetworkURLCatalogItem extends NetworkCatalogItem {
	/**
	 * Creates new NetworkURLCatalogItem instance with specified accessibility and type.
	 *
	 * @param link          corresponding NetworkLink object. Must be not <code>null</code>.
	 * @param title         title of this library item. Must be not <code>null</code>.
	 * @param summary       description of this library item. Can be <code>null</code>.
	 * @param urls          collection of item-related URLs. Can be <code>null</code>.
	 * @param accessibility value defines when this library item will be accessible
	 *                      in the network library view.
	 * @param flags         value defines how to show book items in this catalog.
	 */
	protected NetworkURLCatalogItem(INetworkLink link, CharSequence title, CharSequence summary, UrlInfoCollection<?> urls, Accessibility accessibility, int flags) {
		super(link, title, summary, urls, accessibility, flags);
	}

	protected String getCatalogUrl() {
		return getUrl(UrlInfo.Type.Catalog);
	}

	@Override
	public boolean canBeOpened() {
		return getCatalogUrl() != null;
	}

	@Override
	public String getStringId() {
		String id = getUrl(UrlInfo.Type.Catalog);
		if (id == null) {
			id = getUrl(UrlInfo.Type.HtmlPage);
		}
		return id != null ? id : String.valueOf(hashCode());
	}
}
