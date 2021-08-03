package com.longluo.ebookreader.book;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import com.longluo.zlibrary.core.filesystem.ZLFile;
import com.longluo.zlibrary.core.image.ZLImage;

import com.longluo.ebookreader.formats.IFormatPluginCollection;

public abstract class CoverUtil {
	private static final WeakReference<ZLImage> NULL_IMAGE = new WeakReference<ZLImage>(null);
	private static final WeakHashMap<ZLFile,WeakReference<ZLImage>> ourCovers =
		new WeakHashMap<ZLFile,WeakReference<ZLImage>>();

	public static ZLImage getCover(AbstractBook book, IFormatPluginCollection collection) {
		if (book == null) {
			return null;
		}
		synchronized (book) {
			return getCover(ZLFile.createFileByPath(book.getPath()), collection);
		}
	}

	public static ZLImage getCover(ZLFile file, IFormatPluginCollection collection) {
		WeakReference<ZLImage> cover = ourCovers.get(file);
		if (cover == NULL_IMAGE) {
			return null;
		} else if (cover != null) {
			final ZLImage image = cover.get();
			if (image != null) {
				return image;
			}
		}
		ZLImage image = null;
		try {
			image = collection.getPlugin(file).readCover(file);
		} catch (Exception e) {
			// ignore
		}
		ourCovers.put(file, image != null ? new WeakReference<ZLImage>(image) : NULL_IMAGE);
		return image;
	}
}
