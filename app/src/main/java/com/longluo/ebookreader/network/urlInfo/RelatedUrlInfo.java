package com.longluo.ebookreader.network.urlInfo;

import com.longluo.zlibrary.core.util.MimeType;

public class RelatedUrlInfo extends UrlInfo {
	private static final long serialVersionUID = -893514485257788098L;

	public final String Title;

	public RelatedUrlInfo(Type type, String title, String url, MimeType mime) {
		super(type, url, mime);
		Title = title;
	}
}
