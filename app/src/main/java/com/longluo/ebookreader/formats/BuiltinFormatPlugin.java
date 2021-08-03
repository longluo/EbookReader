package com.longluo.ebookreader.formats;

import com.longluo.zlibrary.core.util.SystemInfo;

import com.longluo.ebookreader.bookmodel.BookModel;

public abstract class BuiltinFormatPlugin extends FormatPlugin {
	protected BuiltinFormatPlugin(SystemInfo systemInfo, String fileType) {
		super(systemInfo, fileType);
	}

	public abstract void readModel(BookModel model) throws BookReadingException;
}
