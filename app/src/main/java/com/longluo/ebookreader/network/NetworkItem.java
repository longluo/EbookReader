package com.longluo.ebookreader.network;

import java.util.*;

import com.longluo.ebookreader.network.urlInfo.UrlInfo;
import com.longluo.ebookreader.network.urlInfo.UrlInfoCollection;

public abstract class NetworkItem {
	public final INetworkLink Link;
	public final CharSequence Title;

	private CharSequence mySummary;
	private final UrlInfoCollection<UrlInfo> myURLs;

	/**
	 * Creates new NetworkItem instance.
	 *
	 * @param link       corresponding NetworkLink object. Must be not <code>null</code>.
	 * @param title      title of this library item. Must be not <code>null</code>.
	 * @param summary    description of this library item. Can be <code>null</code>.
	 * @param urls       collection of item-related urls (like icon link, opds catalog link, etc. Can be <code>null</code>.
	 */
	protected NetworkItem(INetworkLink link, CharSequence title, CharSequence summary, UrlInfoCollection<?> urls) {
		Link = link;
		Title = title != null ? title : "";
		setSummary(summary);
		if (urls != null && !urls.isEmpty()) {
 			myURLs = new UrlInfoCollection<UrlInfo>(urls);
		} else {
			myURLs = null;
		}
	}

	protected void setSummary(CharSequence summary) {
		mySummary = summary;
	}

	public CharSequence getSummary() {
		return mySummary;
	}

	protected void addUrls(UrlInfoCollection<?> urls) {
		myURLs.upgrade(urls);
	}

	public List<UrlInfo> getAllInfos() {
		if (myURLs == null) {
			return Collections.emptyList();
		}
		return myURLs.getAllInfos();
	}

	public List<UrlInfo> getAllInfos(UrlInfo.Type type) {
		if (myURLs == null) {
			return Collections.emptyList();
		}
		return myURLs.getAllInfos(type);
	}

	public String getUrl(UrlInfo.Type type) {
		if (myURLs == null) {
			return null;
		}
		return myURLs.getUrl(type);
	}
}
