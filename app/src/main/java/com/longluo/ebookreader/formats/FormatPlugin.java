package com.longluo.ebookreader.formats;

import java.util.Collections;
import java.util.List;

import com.longluo.zlibrary.core.drm.FileEncryptionInfo;
import com.longluo.zlibrary.core.encodings.EncodingCollection;
import com.longluo.zlibrary.core.filesystem.ZLFile;
import com.longluo.zlibrary.core.image.ZLImage;
import com.longluo.zlibrary.core.resources.ZLResource;
import com.longluo.zlibrary.core.util.SystemInfo;

import com.longluo.ebookreader.book.AbstractBook;

public abstract class FormatPlugin {
	protected final SystemInfo SystemInfo;
	private final String myFileType;

	protected FormatPlugin(SystemInfo systemInfo, String fileType) {
		SystemInfo = systemInfo;
		myFileType = fileType;
	}

	public final String supportedFileType() {
		return myFileType;
	}

	public final String name() {
		return ZLResource.resource("format").getResource(myFileType).getValue();
	}

	public ZLFile realBookFile(ZLFile file) throws BookReadingException {
		return file;
	}
	public List<FileEncryptionInfo> readEncryptionInfos(AbstractBook book) {
		return Collections.emptyList();
	}
	public abstract void readMetainfo(AbstractBook book) throws BookReadingException;
	public abstract void readUids(AbstractBook book) throws BookReadingException;
	public abstract void detectLanguageAndEncoding(AbstractBook book) throws BookReadingException;
	public abstract ZLImage readCover(ZLFile file);
	public abstract String readAnnotation(ZLFile file);

	/* lesser is higher: 0 for ePub/fb2, 5 for other native, 10 for external */
	public abstract int priority();

	public abstract EncodingCollection supportedEncodings();
}
