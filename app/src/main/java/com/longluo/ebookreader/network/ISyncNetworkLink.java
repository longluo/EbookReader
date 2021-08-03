package com.longluo.ebookreader.network;

import com.longluo.zlibrary.core.network.ZLNetworkContext;

public interface ISyncNetworkLink extends INetworkLink {
	boolean isLoggedIn(ZLNetworkContext context);
	void logout(ZLNetworkContext context);
}
