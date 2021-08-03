package com.longluo.ebookreader.network;

import com.longluo.zlibrary.core.network.ZLNetworkContext;
import com.longluo.zlibrary.core.network.ZLNetworkException;
import com.longluo.zlibrary.core.util.MimeType;

import com.longluo.ebookreader.network.urlInfo.*;

public interface ICustomNetworkLink extends INetworkLink {
	void setTitle(String title);
	void setSummary(String summary);

	UrlInfoCollection<UrlInfoWithDate> urlInfoMap();
	void setUrl(UrlInfo.Type type, String url, MimeType mime);
	void removeUrl(UrlInfo.Type type);

	boolean isObsolete(long milliSeconds);
	void reloadInfo(ZLNetworkContext nc, boolean urlsOnly, boolean quietly) throws ZLNetworkException;

	// returns true if next methods have changed link's data:
	//   setTitle, setSummary, setIcon, setLink, removeLink
	boolean hasChanges();

	// resets hasChanged() result
	void resetChanges();
}
