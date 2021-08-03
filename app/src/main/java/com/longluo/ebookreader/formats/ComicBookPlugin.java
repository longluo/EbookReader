package com.longluo.ebookreader.formats;

import com.longluo.zlibrary.core.util.SystemInfo;

import com.longluo.ebookreader.book.AbstractBook;
import com.longluo.ebookreader.book.BookUtil;

public class ComicBookPlugin extends ExternalFormatPlugin {
	public ComicBookPlugin(SystemInfo systemInfo) {
		super(systemInfo, "CBZ");
	}

	@Override
	public String packageName() {
		return "com.longluo.ebookreader.plugin.comicbook";
	}

	@Override
	public void readMetainfo(AbstractBook book) {
		// TODO: implement
	}

	@Override
	public void readUids(AbstractBook book) {
		if (book.uids().isEmpty()) {
			book.addUid(BookUtil.createUid(book, "SHA-256"));
		}
	}
}
