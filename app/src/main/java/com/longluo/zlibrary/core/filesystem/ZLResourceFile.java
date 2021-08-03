package com.longluo.zlibrary.core.filesystem;

import java.util.*;

import com.longluo.zlibrary.core.library.ZLibrary;

public abstract class ZLResourceFile extends ZLFile {
	private static Map<String,ZLResourceFile> ourCache =
		Collections.synchronizedMap(new HashMap<String,ZLResourceFile>());

	public static ZLResourceFile createResourceFile(String path) {
		ZLResourceFile file = ourCache.get(path);
		if (file == null) {
			file = ZLibrary.Instance().createResourceFile(path);
			ourCache.put(path, file);
		}
		return file;
	}

	static ZLResourceFile createResourceFile(ZLResourceFile parent, String name) {
		return ZLibrary.Instance().createResourceFile(parent, name);
	}

	private final String myPath;

	protected ZLResourceFile(String path) {
		myPath = path;
		init();
	}

	@Override
	public String getPath() {
		return myPath;
	}

	@Override
	public String getLongName() {
		return myPath.substring(myPath.lastIndexOf('/') + 1);
	}

	@Override
	public ZLPhysicalFile getPhysicalFile() {
		return null;
	}
}
