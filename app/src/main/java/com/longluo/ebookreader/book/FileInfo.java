package com.longluo.ebookreader.book;

import com.longluo.zlibrary.core.tree.ZLTree;

public final class FileInfo extends ZLTree<FileInfo> {
	public final String Name;
	public long Id;
	public long FileSize = -1;

	FileInfo(String name, FileInfo parent) {
		this(name, parent, -1);
	}

	FileInfo(String name, FileInfo parent, long id) {
		super(parent);
		Name = name;
		Id = id;
	}
}
