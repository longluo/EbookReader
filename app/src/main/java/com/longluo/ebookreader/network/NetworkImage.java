package com.longluo.ebookreader.network;

import java.io.File;

import android.net.Uri;

import com.longluo.zlibrary.core.filesystem.ZLFile;
import com.longluo.zlibrary.core.image.*;
import com.longluo.zlibrary.core.network.QuietNetworkContext;
import com.longluo.zlibrary.core.network.ZLNetworkException;
import com.longluo.zlibrary.core.util.MimeType;
import com.longluo.zlibrary.core.util.SystemInfo;

public final class NetworkImage extends ZLImageSimpleProxy {
	public final String Url;
	private final SystemInfo mySystemInfo;

	public NetworkImage(String url, SystemInfo systemInfo) {
		Url = url;
		mySystemInfo = systemInfo;
		new File(systemInfo.networkCacheDirectory()).mkdirs();
	}

	private static final String TOESCAPE = "<>:\"|?*\\";

	private String makeImageFilePath(String url) {
		final Uri uri = Uri.parse(url);

		final StringBuilder path = new StringBuilder(mySystemInfo.networkCacheDirectory());
		path.append(File.separator);

		final String host = uri.getHost();
		path.append(host != null ? host : "host.unknown");

		int index = path.length();

		final String uriPath = uri.getPath();
		if (uriPath != null) {
			path.append(uriPath);
		}

		int nameIndex = index;
		while (index < path.length()) {
			char ch = path.charAt(index);
			if (TOESCAPE.indexOf(ch) != -1) {
				path.setCharAt(index, '_');
			}
			if (ch == '/') {
				if (index + 1 == path.length()) {
					path.deleteCharAt(index);
				} else {
					path.setCharAt(index, '_');
					nameIndex = index + 1;
				}
			}
			++index;
		}

		String query = uri.getQuery();
		if (query != null) {
			index = 0;
			while (index < query.length()) {
				int j = query.indexOf("&", index);
				if (j == -1) {
					j = query.length();
				}
				String param = query.substring(index, j);
				if (!param.startsWith("username=")
					&& !param.startsWith("password=")
					&& !param.endsWith("=")) {
					int k = path.length();
					path.append("_").append(param);
					while (k < path.length()) {
						char ch = path.charAt(k);
						if (TOESCAPE.indexOf(ch) != -1 || ch == '/') {
							path.setCharAt(k, '_');
						}
						++k;
					}
				}
				index = j + 1;
			}
		}
		return path.toString();
	}

	private volatile String myStoredFilePath;
	private String getFilePath() {
		if (myStoredFilePath == null) {
			myStoredFilePath = makeImageFilePath(Url);
		}
		return myStoredFilePath;
	}

	@Override
	protected boolean isOutdated() {
		return !new File(getFilePath()).exists();
	}

	@Override
	public SourceType sourceType() {
		return SourceType.NETWORK;
	}

	@Override
	public String getId() {
		return Url;
	}

	public String getURI() {
		// TODO: implement
		return null;
	}

	@Override
	public void synchronize() {
		synchronizeInternal(false);
	}

	public void synchronizeFast() {
		synchronizeInternal(true);
	}

	private final synchronized void synchronizeInternal(boolean doFast) {
		if (isSynchronized()) {
			return;
		}
		try {
			final String path = getFilePath();
			if (path == null) {
				// TODO: error message ???
				return;
			}
			final int index = path.lastIndexOf(File.separator);
			if (index != -1) {
				final String dir = path.substring(0, index);
				final File dirFile = new File(dir);
				if (!dirFile.exists() && !dirFile.mkdirs()) {
					// TODO: error message ???
					return;
				}
				if (!dirFile.exists() || !dirFile.isDirectory()) {
					// TODO: error message ???
					return;
				}
			}
			final File imageFile = new File(path);
			if (imageFile.exists()) {
				final long diff = System.currentTimeMillis() - imageFile.lastModified();
				final long valid = 24 * 60 * 60 * 1000; // one day in milliseconds; FIXME: hardcoded const
				if (diff >= 0 && diff <= valid) {
					return;
				}
				imageFile.delete();
			}
			if (doFast) {
				return;
			}
			new QuietNetworkContext().downloadToFileQuietly(Url, imageFile);
		} finally {
			setSynchronized();
		}
	}

	private volatile ZLFileImage myFileImage;
	@Override
	public ZLFileImage getRealImage() {
		if (myFileImage == null) {
			if (!isSynchronized()) {
				return null;
			}
			final String path = getFilePath();
			if (path == null) {
				return null;
			}
			final ZLFile file = ZLFile.createFileByPath(path);
			if (file == null) {
				return null;
			}
			myFileImage = new ZLFileImage(file);
		}
		return myFileImage;
	}
}
