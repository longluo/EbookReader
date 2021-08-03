package com.longluo.ebookreader.book;

import java.util.*;

import com.longluo.util.ComparisonUtil;

import com.longluo.zlibrary.core.filesystem.ZLFile;
import com.longluo.zlibrary.core.filesystem.ZLPhysicalFile;

class DuplicateResolver {
	private final Map<String,List<ZLFile>> myMap =
		Collections.synchronizedMap(new HashMap<String,List<ZLFile>>());

	void addFile(ZLFile file) {
		final String key = file.getShortName();
		List<ZLFile> list;
		synchronized (myMap) {
			list = myMap.get(key);
			if (list == null) {
				list = new LinkedList<ZLFile>();
				myMap.put(key, list);
			}
		}
		synchronized (list) {
			if (!list.contains(file)) {
				list.add(file);
			}
		}
	}

	void removeFile(ZLFile file) {
		final List<ZLFile> list = myMap.get(file.getShortName());
		if (list != null) {
			synchronized (list) {
				list.remove(file);
			}
		}
	}

	private String entryName(ZLFile file) {
		final String path = file.getPath();
		final int index = path.indexOf(":");
		return index == -1 ? null : path.substring(index + 1);
	}

	ZLFile findDuplicate(ZLFile file) {
		final ZLPhysicalFile pFile = file.getPhysicalFile();
		if (pFile == null) {
			return null;
		}
		final List<ZLFile> list = myMap.get(file.getShortName());
		if (list == null || list.isEmpty()) {
			return null;
		}
		final List<ZLFile> copy;
		synchronized (list) {
			copy = new ArrayList<ZLFile>(list);
		}

		final String entryName = entryName(file);
		final String shortName = pFile.getShortName();
		final long size = pFile.size();
		final long lastModified = pFile.javaFile().lastModified();
		for (ZLFile candidate : copy) {
			if (file.equals(candidate)) {
				return candidate;
			}
			final ZLPhysicalFile pCandidate = candidate.getPhysicalFile();
			if (pCandidate != null &&
				ComparisonUtil.equal(entryName, entryName(candidate)) &&
				shortName.equals(pCandidate.getShortName()) &&
				size == pCandidate.size() &&
				lastModified == pCandidate.javaFile().lastModified()
			) {
				return candidate;
			}
		}
		return null;
	}
}
