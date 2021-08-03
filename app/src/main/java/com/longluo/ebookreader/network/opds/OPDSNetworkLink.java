package com.longluo.ebookreader.network.opds;

import java.util.*;
import java.net.URLEncoder;
import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.longluo.zlibrary.core.network.ZLNetworkException;
import com.longluo.zlibrary.core.network.ZLNetworkRequest;
import com.longluo.zlibrary.core.util.MimeType;

import com.longluo.ebookreader.network.*;
import com.longluo.ebookreader.network.authentication.NetworkAuthenticationManager;
import com.longluo.ebookreader.network.urlInfo.*;
import com.longluo.ebookreader.network.tree.NetworkItemsLoader;

public abstract class OPDSNetworkLink extends AbstractNetworkLink {
	protected final NetworkLibrary myLibrary;

	private TreeMap<RelationAlias,String> myRelationAliases;

	private final LinkedList<URLRewritingRule> myUrlRewritingRules = new LinkedList<URLRewritingRule>();
	private final Map<String,String> myExtraData = new HashMap<String,String>();
	private NetworkAuthenticationManager myAuthenticationManager;

	OPDSNetworkLink(NetworkLibrary library, int id, String title, String summary, String language,
			UrlInfoCollection<UrlInfoWithDate> infos) {
		super(id, title, summary, language, infos);
		myLibrary = library;
	}

	final void setRelationAliases(Map<RelationAlias,String> relationAliases) {
		if (relationAliases != null && relationAliases.size() > 0) {
			myRelationAliases = new TreeMap<RelationAlias,String>(relationAliases);
		} else {
			myRelationAliases = null;
		}
	}

	final void setUrlRewritingRules(List<URLRewritingRule> rules) {
		myUrlRewritingRules.clear();
		myUrlRewritingRules.addAll(rules);
	}

	final void setExtraData(Map<String,String> extraData) {
		myExtraData.clear();
		myExtraData.putAll(extraData);
	}

	final void setAuthenticationManager(NetworkAuthenticationManager mgr) {
		myAuthenticationManager = mgr;
	}

	/*
	public AccountStatus getAccountStatus(boolean force) {
		if (myAuthenticationManager == null) {
			return AccountStatus.NotSupported;
		}
		if ("".equals(myAuthenticationManager.UserNameOption.getValue())) {
			return AccountStatus.NoUserName;
		}
	}
		SignedIn,
		SignedOut,
		NotChecked
	*/

	ZLNetworkRequest createNetworkData(String url, final OPDSCatalogItem.State state) {
		if (url == null) {
			return null;
		}
		final NetworkCatalogItem catalogItem = state.Loader.Tree.Item;
		myLibrary.startLoading(catalogItem);
		url = rewriteUrl(url, false);
		return new ZLNetworkRequest.Get(url) {
			@Override
			public void handleStream(InputStream inputStream, int length) throws IOException, ZLNetworkException {
				if (state.Loader.confirmInterruption()) {
					return;
				}

				new OPDSXMLReader(
					myLibrary, new OPDSFeedHandler(myLibrary, getURL(), state), false
				).read(inputStream);

				if (state.Loader.confirmInterruption() && state.LastLoadedId != null) {
					// reset state to load current page from the beginning
					state.LastLoadedId = null;
				} else {
					state.Loader.Tree.confirmAllItems();
				}
			}

			@Override
			public void doAfter(boolean success) {
				myLibrary.stopLoading(catalogItem);
			}
		};
	}

	@Override
	public OPDSCatalogItem.State createOperationData(NetworkItemsLoader loader) {
		return new OPDSCatalogItem.State(this, loader);
	}

	public ZLNetworkRequest simpleSearchRequest(String pattern, NetworkOperationData data) {
		final UrlInfo info = getUrlInfo(UrlInfo.Type.Search);
		if (info == null || info.Url == null || !MimeType.APP_ATOM_XML.weakEquals(info.Mime)) {
			return null;
		}
		try {
			pattern = URLEncoder.encode(pattern, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		return createNetworkData(info.Url.replace("%s", pattern), (OPDSCatalogItem.State)data);
	}

	public ZLNetworkRequest resume(NetworkOperationData data) {
		return createNetworkData(data.ResumeURI, (OPDSCatalogItem.State)data);
	}

	public NetworkCatalogItem libraryItem() {
		final UrlInfoCollection<UrlInfo> urlMap = new UrlInfoCollection<UrlInfo>();
		urlMap.addInfo(getUrlInfo(UrlInfo.Type.Catalog));
		urlMap.addInfo(getUrlInfo(UrlInfo.Type.Image));
		urlMap.addInfo(getUrlInfo(UrlInfo.Type.Thumbnail));
		return new OPDSCatalogItem(
			this,
			getTitle(),
			getSummary(),
			urlMap,
			OPDSCatalogItem.Accessibility.ALWAYS,
			OPDSCatalogItem.FLAGS_DEFAULT | OPDSCatalogItem.FLAG_ADD_SEARCH_ITEM,
			myExtraData
		) {
			@Override
			public String getSummary() {
				return OPDSNetworkLink.this.getSummary();
			}
		};
	}

	public NetworkAuthenticationManager authenticationManager() {
		return myAuthenticationManager;
	}

	public String rewriteUrl(String url, boolean isUrlExternal) {
		final int apply = isUrlExternal
			? URLRewritingRule.APPLY_EXTERNAL : URLRewritingRule.APPLY_INTERNAL;
		for (URLRewritingRule rule : myUrlRewritingRules) {
			if ((rule.whereToApply() & apply) != 0) {
				url = rule.apply(url);
			}
		}
		return url;
	}

	// rel and type must be either null or interned String objects.
	String relation(String rel, MimeType type) {
		if (myRelationAliases == null) {
			return rel;
		}
		RelationAlias alias = new RelationAlias(rel, type.Name);
		String mapped = myRelationAliases.get(alias);
		if (mapped != null) {
			return mapped;
		}
		if (type != null) {
			alias = new RelationAlias(rel, null);
			mapped = myRelationAliases.get(alias);
			if (mapped != null) {
				return mapped;
			}
		}
		return rel;
	}

	private BasketItem myBasketItem;

	@Override
	public BasketItem getBasketItem() {
		final String url = getUrl(UrlInfo.Type.ListBooks);
		if (url != null && myBasketItem == null) {
			myBasketItem = new OPDSBasketItem(myLibrary, this);
		}
		return myBasketItem;
	}

	@Override
	public String toString() {
		return "OPDSNetworkLink: {super=" + super.toString()
			+ "; authManager=" + (myAuthenticationManager != null ? myAuthenticationManager.getClass().getName() : null)
			+ "; relationAliases=" + myRelationAliases
			+ "; rewritingRules=" + myUrlRewritingRules
			+ "}";
	}
}
