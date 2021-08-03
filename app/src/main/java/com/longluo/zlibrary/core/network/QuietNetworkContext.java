package com.longluo.zlibrary.core.network;

import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

public class QuietNetworkContext extends ZLNetworkContext {
	@Override
	public Map<String,String> authenticate(URI uri, String realm, Map<String,String> params) {
		return Collections.singletonMap("error", "Required authorization");
	}

	public final boolean downloadToFileQuietly(String url, final File outFile) {
		try {
			downloadToFile(url, outFile);
			return true;
		} catch (ZLNetworkException e) {
			return false;
		}
	}
}
