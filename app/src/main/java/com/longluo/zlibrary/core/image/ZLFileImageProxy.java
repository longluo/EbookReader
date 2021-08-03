package com.longluo.zlibrary.core.image;

import com.longluo.zlibrary.core.filesystem.ZLFile;

public abstract class ZLFileImageProxy extends ZLImageSimpleProxy {
	protected final ZLFile File;
	private volatile ZLFileImage myImage;

	protected ZLFileImageProxy(ZLFile file) {
		File = file;
	}

	@Override
	public final ZLFileImage getRealImage() {
		return myImage;
	}

	@Override
	public String getURI() {
		return "cover:" + File.getPath();
	}

	@Override
	public final synchronized void synchronize() {
		if (myImage == null) {
			myImage = retrieveRealImage();
			setSynchronized();
		}
	}

	@Override
	public SourceType sourceType() {
		return SourceType.FILE;
	}

	@Override
	public String getId() {
		return File.getPath();
	}

	protected abstract ZLFileImage retrieveRealImage();
}
