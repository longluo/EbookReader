package com.longluo.ebookreader.network;

import com.longluo.zlibrary.core.network.ZLNetworkRequest;

import com.longluo.ebookreader.network.tree.NetworkItemsLoader;

public class NetworkOperationData {
	public final INetworkLink Link;
	public volatile NetworkItemsLoader Loader;
	public volatile String ResumeURI;

	public NetworkOperationData(INetworkLink link, NetworkItemsLoader loader) {
		Link = link;
		Loader = loader;
	}

	protected void clear() {
		ResumeURI = null;
	}

	public final ZLNetworkRequest resume() {
		final ZLNetworkRequest request = Link.resume(this);
		clear();
		return request;
	}
}
