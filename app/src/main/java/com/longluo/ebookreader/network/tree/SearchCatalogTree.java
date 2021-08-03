package com.longluo.ebookreader.network.tree;

import com.longluo.util.Pair;

import com.longluo.zlibrary.core.image.ZLImage;
import com.longluo.zlibrary.core.network.ZLNetworkContext;
import com.longluo.zlibrary.core.util.MimeType;

import com.longluo.ebookreader.network.*;
import com.longluo.ebookreader.network.urlInfo.UrlInfo;

public class SearchCatalogTree extends NetworkCatalogTree {
	public SearchCatalogTree(RootTree parent, SearchItem item) {
		super(parent, null, item, -1);
		item.setPattern(null);
	}

	public SearchCatalogTree(NetworkCatalogTree parent, SearchItem item) {
		super(parent, parent.getLink(), item, -1);
		item.setPattern(null);
	}

	public void setPattern(String pattern) {
		((SearchItem)Item).setPattern(pattern);
	}

	@Override
	protected boolean canUseParentCover() {
		return false;
	}

	@Override
	public boolean isContentValid() {
		return true;
	}

	@Override
	public String getName() {
		final String pattern = ((SearchItem)Item).getPattern();
		if (pattern != null && Library.getStoredLoader(this) == null) {
			return NetworkLibrary.resource().getResource("found").getValue();
		}
		return super.getName();
	}

	@Override
	public Pair<String,String> getTreeTitle() {
		return new Pair(getSummary(), null);
	}

	@Override
	public String getSummary() {
		final String pattern = ((SearchItem)Item).getPattern();
		if (pattern != null) {
			return NetworkLibrary.resource().getResource("found").getResource("summary").getValue().replace("%s", pattern);
		}
		if (Library.getStoredLoader(this) != null) {
			return NetworkLibrary.resource().getResource("search").getResource("summaryInProgress").getValue();
		}
		return super.getSummary();
	}

	public MimeType getMimeType() {
		return ((SearchItem)Item).getMimeType();
	}

	public String getUrl(String pattern) {
		return ((SearchItem)Item).getUrl(pattern);
	}

	public void startItemsLoader(ZLNetworkContext nc, String pattern) {
		new Searcher(nc, this, pattern).start();
	}

	@Override
	public ZLImage createCover() {
		final INetworkLink link = getLink();
		if (link == null) {
			return null;
		}
		final UrlInfo info = link.getUrlInfo(UrlInfo.Type.SearchIcon);
		return info != null ? createCoverFromUrl(Library, info.Url, info.Mime) : null;
	}
}
