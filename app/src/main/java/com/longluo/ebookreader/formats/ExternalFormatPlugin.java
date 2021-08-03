package com.longluo.ebookreader.formats;

import com.longluo.zlibrary.core.encodings.AutoEncodingCollection;
import com.longluo.zlibrary.core.filesystem.ZLFile;
import com.longluo.zlibrary.core.util.SystemInfo;

import com.longluo.ebookreader.book.AbstractBook;

public abstract class ExternalFormatPlugin extends FormatPlugin {
	protected ExternalFormatPlugin(SystemInfo systemInfo, String fileType) {
		super(systemInfo, fileType);
	}

	@Override
	public int priority() {
		return 10;
	}

	public abstract String packageName();

	@Override
	public PluginImage readCover(ZLFile file) {
		return new PluginImage(file, this);
	}

	@Override
	public AutoEncodingCollection supportedEncodings() {
		return new AutoEncodingCollection();
	}

	@Override
	public void detectLanguageAndEncoding(AbstractBook book) {
	}

	@Override
	public String readAnnotation(ZLFile file) {
		return null;
	}

	@Override
	public String toString() {
		return "ExternalFormatPlugin [" + supportedFileType() + "]";
	}
}
