package com.longluo.ebookreader.formats;

import com.longluo.zlibrary.core.filesystem.ZLFile;
import com.longluo.zlibrary.core.image.ZLImage;
import com.longluo.zlibrary.core.image.ZLImageProxy;

public final class PluginImage extends ZLImageProxy {
	public final ZLFile File;
	public final ExternalFormatPlugin Plugin;
	private volatile ZLImage myImage;

	PluginImage(ZLFile file, ExternalFormatPlugin plugin) {
		File = file;
		Plugin = plugin;
	}

	public final synchronized void setRealImage(ZLImage image) {
		if (image != null && !isSynchronized()) {
			myImage = image;
			setSynchronized();
		}
	}

	@Override
	public final ZLImage getRealImage() {
		return myImage;
	}

	@Override
	public SourceType sourceType() {
		return SourceType.SERVICE;
	}

	@Override
	public String getURI() {
		return "cover:" + File.getPath();
	}

	@Override
	public String getId() {
		return File.getPath();
	}
}
