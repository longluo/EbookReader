package com.longluo.android.ebookreader.httpd;

import com.longluo.zlibrary.core.filesystem.ZLFile;

public abstract class DataUtil {
	static ZLFile fileFromEncodedPath(String encodedPath) {
		final StringBuilder path = new StringBuilder();
		for (String item : encodedPath.split("X")) {
			if (item.length() == 0) {
				continue;
			}
			path.append((char)Short.parseShort(item, 16));
		}
		return ZLFile.createFileByPath(path.toString());
	}

	public static String buildUrl(DataService.Connection connection, String prefix, String path) {
		final int port = connection.getPort();
		if (port == -1) {
			return null;
		}
		final StringBuilder url = new StringBuilder("http://127.0.0.1:").append(port)
			.append("/").append(prefix).append("/");
		for (int i = 0; i < path.length(); ++i) {
			url.append(String.format("X%X", (short)path.charAt(i)));
		}
		return url.toString();
	}
}
