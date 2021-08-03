package com.longluo.ebookreader.network.authentication.litres;

import com.longluo.zlibrary.core.xml.ZLStringMap;
import com.longluo.zlibrary.core.network.ZLNetworkAuthenticationException;
import com.longluo.zlibrary.core.network.ZLNetworkException;

import com.longluo.ebookreader.network.AlreadyPurchasedException;
import com.longluo.ebookreader.network.NetworkException;

class LitResPurchaseXMLReader extends LitResAuthenticationXMLReader {
	private static final String TAG_AUTHORIZATION_FAILED = "catalit-authorization-failed";
	private static final String TAG_PURCHASE_OK = "catalit-purchase-ok";
	private static final String TAG_PURCHASE_FAILED = "catalit-purchase-failed";

	public String Account;
	public String BookId;

	@Override
	public boolean startElementHandler(String tag, ZLStringMap attributes) {
		tag = tag.toLowerCase().intern();
		if (TAG_AUTHORIZATION_FAILED == tag) {
			setException(new ZLNetworkAuthenticationException());
		} else {
			Account = attributes.getValue("account");
			BookId = attributes.getValue("art");
			if (TAG_PURCHASE_OK == tag) {
				// nop
			} else if (TAG_PURCHASE_FAILED == tag) {
				final String error = attributes.getValue("error");
				if ("1".equals(error)) {
					setException(ZLNetworkException.forCode(NetworkException.ERROR_PURCHASE_NOT_ENOUGH_MONEY));
				} else if ("2".equals(error)) {
					setException(ZLNetworkException.forCode(NetworkException.ERROR_PURCHASE_MISSING_BOOK));
				} else if ("3".equals(error)) {
					setException(new AlreadyPurchasedException());
				} else {
					setException(ZLNetworkException.forCode(NetworkException.ERROR_INTERNAL));
				}
			} else {
				setException(ZLNetworkException.forCode(NetworkException.ERROR_SOMETHING_WRONG, LitResUtil.HOST_NAME));
			}
		}
		return true;
	}
}
