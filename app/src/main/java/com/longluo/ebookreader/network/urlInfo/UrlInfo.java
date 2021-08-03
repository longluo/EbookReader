package com.longluo.ebookreader.network.urlInfo;

import java.io.Serializable;

import com.longluo.util.ComparisonUtil;

import com.longluo.zlibrary.core.util.MimeType;

public class UrlInfo implements Serializable {
	private static final long serialVersionUID = -893514485257788222L;

	public static enum Type {
		// Never rename elements of this enum; these names are used in network database
		Catalog,
		HtmlPage,
		SingleEntry,
		Related,
		Image,
		Thumbnail,
		SearchIcon,
		Search,
		ListBooks,
		SignIn,
		SignOut,
		SignUp,
		TopUp,
		RecoverPassword,
		Book,
		BookConditional,
		BookDemo,
		BookFullOrDemo,
		BookBuy,
		BookBuyInBrowser,
		TOC,
		Comments
	}

	public final Type InfoType;
	public final String Url;
	public final MimeType Mime;

	public UrlInfo(Type type, String url, MimeType mime) {
		InfoType = type;
		Url = url;
		Mime = mime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof UrlInfo)) {
			return false;
		}
		final UrlInfo info = (UrlInfo)o;
		return InfoType == info.InfoType && ComparisonUtil.equal(Url, info.Url);
	}

	@Override
	public int hashCode() {
		return InfoType.hashCode() + ComparisonUtil.hashCode(Url);
	}
}
