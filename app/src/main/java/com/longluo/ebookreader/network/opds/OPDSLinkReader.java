package com.longluo.ebookreader.network.opds;

import java.util.*;
import java.io.*;

import com.longluo.zlibrary.core.filesystem.ZLPhysicalFile;
import com.longluo.zlibrary.core.network.*;

import com.longluo.ebookreader.network.*;

public class OPDSLinkReader {
	static final String CATALOGS_URL = "http://data.fbreader.org/catalogs/generic-2.0.xml";
	static final String FILE_NAME = "fbreader_catalogs-"
			+ CATALOGS_URL.substring(CATALOGS_URL.lastIndexOf("/") + 1);

	public enum CacheMode {
		LOAD,
		UPDATE,
		CLEAR
	};

	public static List<INetworkLink> loadOPDSLinks(NetworkLibrary library, ZLNetworkContext nc, CacheMode cacheMode) throws ZLNetworkException {
		final OPDSLinkXMLReader xmlReader = new OPDSLinkXMLReader(library);

		final File dirFile = new File(library.SystemInfo.networkCacheDirectory());
		if (!dirFile.exists() && !dirFile.mkdirs()) {
			nc.perform(new ZLNetworkRequest.Get(CATALOGS_URL) {
				@Override
				public void handleStream(InputStream inputStream, int length) throws IOException, ZLNetworkException {
					xmlReader.read(inputStream);
				}
			});
			return xmlReader.links();
		}

		boolean cacheIsGood = false;
		File oldCache = null;
		final File catalogsFile = new File(dirFile, FILE_NAME);
		if (catalogsFile.exists()) {
			switch (cacheMode) {
				case UPDATE:
					final long diff = System.currentTimeMillis() - catalogsFile.lastModified();
					if (diff >= 0 && diff <= 7 * 24 * 60 * 60 * 1000) { // one week
						return Collections.emptyList();
					}
					/* FALLTHROUGH */
				case CLEAR:
					oldCache = new File(dirFile, "_" + FILE_NAME);
					oldCache.delete();
					if (!catalogsFile.renameTo(oldCache)) {
						catalogsFile.delete();
						oldCache = null;
					}
					break;
				case LOAD:
					cacheIsGood = true;
					break;
			}
		}

		if (!cacheIsGood) {
			try {
				nc.downloadToFile(CATALOGS_URL, catalogsFile);
			} catch (ZLNetworkException e) {
				if (oldCache == null) {
					throw e;
				}
				catalogsFile.delete();
				if (!oldCache.renameTo(catalogsFile)) {
					oldCache.delete();
					oldCache = null;
					throw e;
				}
			} finally {
				if (oldCache != null) {
					oldCache.delete();
					oldCache = null;
				}
			}
		}

		try {
			xmlReader.read(new ZLPhysicalFile(catalogsFile));
			return xmlReader.links();
		} catch (IOException e) {
			return Collections.emptyList();
		}
	}
}
