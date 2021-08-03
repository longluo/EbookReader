package com.longluo.ebookreader.network.rss;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.longluo.zlibrary.core.network.ZLNetworkException;
import com.longluo.zlibrary.core.network.ZLNetworkRequest;
import com.longluo.zlibrary.core.util.MimeType;

import com.longluo.ebookreader.network.*;
import com.longluo.ebookreader.network.urlInfo.*;
import com.longluo.ebookreader.network.authentication.NetworkAuthenticationManager;
import com.longluo.ebookreader.network.tree.NetworkItemsLoader;

public class RSSNetworkLink extends AbstractNetworkLink implements IPredefinedNetworkLink {
	private final String myPredefinedId;
	private final Map<String,String> myExtraData = new HashMap<String,String>();

	public RSSNetworkLink(
		int id, String predefinedId,
		String title, String summary,
		String language, UrlInfoCollection<UrlInfoWithDate> infos
	) {
		super(id, title, summary, language, infos);
		myPredefinedId = predefinedId;
	}

	ZLNetworkRequest createNetworkData(String url, final RSSCatalogItem.State result) {
		if (url == null) {
			return null;
		}

		final NetworkLibrary library = result.Loader.Tree.Library;
		final NetworkCatalogItem catalogItem = result.Loader.Tree.Item;
		library.startLoading(catalogItem);

		return new ZLNetworkRequest.Get(url, false) {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void handleStream(InputStream inputStream, int length) throws IOException, ZLNetworkException {
				if (result.Loader.confirmInterruption()) {
					return;
				}

				new RSSXMLReader(library, new RSSChannelHandler(getURL(), result), false).read(inputStream);

				if (result.Loader.confirmInterruption() && result.LastLoadedId != null) {
					result.LastLoadedId = null;
				} else {
					result.Loader.Tree.confirmAllItems();
				}
			}

			@Override
			public void doAfter(boolean success) {
				library.stopLoading(catalogItem);
			}
		};
	}

	@Override
	public RSSCatalogItem.State createOperationData(NetworkItemsLoader loader) {
		return new RSSCatalogItem.State(this, loader);
	}

	public final void setExtraData(Map<String,String> extraData) {
		myExtraData.clear();
		myExtraData.putAll(extraData);
	}

	@Override
	public Type getType() {
		return Type.Predefined;
	}

	@Override
	public String getPredefinedId() {
		return myPredefinedId;
	}

	@Override
	public ZLNetworkRequest simpleSearchRequest(String pattern, NetworkOperationData data) {
		return null;
	}

	@Override
	public ZLNetworkRequest resume(NetworkOperationData data) {
		return null;
	}

	@Override
	public NetworkCatalogItem libraryItem() {
		final UrlInfoCollection<UrlInfo> urlMap = new UrlInfoCollection<UrlInfo>();
		urlMap.addInfo(getUrlInfo(UrlInfo.Type.Catalog));
		urlMap.addInfo(getUrlInfo(UrlInfo.Type.Image));
		urlMap.addInfo(getUrlInfo(UrlInfo.Type.Thumbnail));
		return new RSSCatalogItem(
			this,
			getTitle(),
			getSummary(),
			urlMap,
			RSSCatalogItem.Accessibility.ALWAYS,
			RSSCatalogItem.FLAGS_DEFAULT | RSSCatalogItem.FLAG_ADD_SEARCH_ITEM
		);
	}

	@Override
	public NetworkAuthenticationManager authenticationManager() {
		return null;
	}

	@Override
	public String rewriteUrl(String url, boolean isUrlExternal) {
		return url;
	}

	public boolean servesHost(String hostname) {
		return false;
	}
}
