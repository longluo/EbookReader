package com.longluo.ebookreader.network.urlInfo;

public class DecoratedBookUrlInfo extends BookUrlInfo {
	private static final long serialVersionUID = 8558634525845586904L;

	private final String myCleanUrl;

	public DecoratedBookUrlInfo(BookUrlInfo base, String url) {
		super(base.InfoType, url, base.Mime);
		myCleanUrl = base.cleanUrl();
	}

	@Override
	public String cleanUrl() {
		return myCleanUrl;
	}
}
