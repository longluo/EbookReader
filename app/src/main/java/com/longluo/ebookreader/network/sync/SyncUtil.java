package com.longluo.ebookreader.network.sync;

import com.longluo.zlibrary.core.network.ZLNetworkContext;

import com.longluo.ebookreader.fbreader.options.SyncOptions;

public abstract class SyncUtil {
	public static String getAccountName(ZLNetworkContext context) {
		return context.getAccountName(SyncOptions.DOMAIN, SyncOptions.REALM);
	}

	public static void logout(ZLNetworkContext context) {
		context.removeCookiesForDomain(SyncOptions.DOMAIN);
		context.setAccountName(SyncOptions.DOMAIN, SyncOptions.REALM, null);
	}
}
