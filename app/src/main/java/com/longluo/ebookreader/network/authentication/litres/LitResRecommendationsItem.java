package com.longluo.ebookreader.network.authentication.litres;

import com.longluo.zlibrary.core.util.ZLNetworkUtil;

import com.longluo.ebookreader.network.*;
import com.longluo.ebookreader.network.opds.OPDSCatalogItem;
import com.longluo.ebookreader.network.opds.OPDSNetworkLink;
import com.longluo.ebookreader.network.urlInfo.*;

public class LitResRecommendationsItem extends OPDSCatalogItem {
	public LitResRecommendationsItem(OPDSNetworkLink link, CharSequence title, CharSequence summary, UrlInfoCollection<?> urls) {
		super(link, title, summary, urls, Accessibility.HAS_BOOKS, FLAGS_DEFAULT & ~FLAGS_GROUP, null);
	}

	@Override
	protected String getCatalogUrl() {
		final LitResAuthenticationManager mgr =
			(LitResAuthenticationManager)Link.authenticationManager();
		final StringBuilder builder = new StringBuilder();
		boolean flag = false;
		for (NetworkBookItem book : mgr.purchasedBooks()) {
			if (flag) {
				builder.append(',');
			} else {
				flag = true;
			}
			builder.append(book.Id);
		}
		final BasketItem basketItem = Link.getBasketItem();
		if (basketItem != null) {
			for (String bookId : basketItem.bookIds()) {
				if (flag) {
					builder.append(',');
				} else {
					flag = true;
				}
				builder.append(bookId);
			}
		}

		return ZLNetworkUtil.appendParameter(getUrl(UrlInfo.Type.Catalog), "ids", builder.toString());
	}
}
