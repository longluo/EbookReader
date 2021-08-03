package com.longluo.ebookreader.network.urlInfo;

import com.longluo.zlibrary.core.money.Money;
import com.longluo.zlibrary.core.util.MimeType;

public class BookBuyUrlInfo extends BookUrlInfo {
	private static final long serialVersionUID = 7877935250896069650L;

	public final Money Price;

	public BookBuyUrlInfo(Type type, String url, MimeType mime, Money price) {
		super(type, url, mime);
		Price = price;
	}
}
