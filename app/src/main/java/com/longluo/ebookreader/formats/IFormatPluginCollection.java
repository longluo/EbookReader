package com.longluo.ebookreader.formats;

import com.longluo.zlibrary.core.filesystem.ZLFile;

public interface IFormatPluginCollection {
	public interface Holder {
		IFormatPluginCollection getCollection();
	}

	FormatPlugin getPlugin(ZLFile file);
}
