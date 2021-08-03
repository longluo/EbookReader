package com.longluo.ebookreader.network.authentication.litres;

import com.longluo.zlibrary.core.util.ZLNetworkUtil;

import com.longluo.ebookreader.network.INetworkLink;

class LitResUtil {
	public static final String HOST_NAME = "litres.ru";

	public static String url(String path) {
		final String url = "://robot.litres.ru/" + path;
		if (ZLNetworkUtil.hasParameter(url, "sid") || ZLNetworkUtil.hasParameter(url, "pwd")) {
			return "https" + url;
		} else {
			return "http" + url;
		}
	}

	public static String url(INetworkLink link, String path) {
		return link.rewriteUrl(url(path), false);
	}
}
