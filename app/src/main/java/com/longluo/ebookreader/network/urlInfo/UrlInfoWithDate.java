package com.longluo.ebookreader.network.urlInfo;

import java.util.Date;

import com.longluo.util.ComparisonUtil;

import com.longluo.zlibrary.core.util.MimeType;

public final class UrlInfoWithDate extends UrlInfo {
	private static final long serialVersionUID = -896768978957787222L;

	public static final UrlInfoWithDate NULL = new UrlInfoWithDate(null, null, MimeType.NULL);

	public final Date Updated;

	public UrlInfoWithDate(Type type, String url, MimeType mime, Date updated) {
		super(type, url, mime);
		Updated = updated;
	}

	public UrlInfoWithDate(Type type, String url, MimeType mime) {
		this(type, url, mime, new Date());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof UrlInfoWithDate)) {
			return false;
		}

		final UrlInfoWithDate info = (UrlInfoWithDate)o;
		return
			InfoType == info.InfoType &&
			ComparisonUtil.equal(Url, info.Url) &&
			ComparisonUtil.equal(Mime, info.Mime) &&
			ComparisonUtil.equal(Updated, info.Updated);
	}

	@Override
	public int hashCode() {
		return InfoType.hashCode() + ComparisonUtil.hashCode(Url) + ComparisonUtil.hashCode(Mime) + ComparisonUtil.hashCode(Updated);
	}
}
