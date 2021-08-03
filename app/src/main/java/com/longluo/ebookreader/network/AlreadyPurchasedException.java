package com.longluo.ebookreader.network;

import com.longluo.zlibrary.core.network.ZLNetworkException;

public class AlreadyPurchasedException extends ZLNetworkException {
	public static final String ERROR_PURCHASE_ALREADY_PURCHASED = "purchaseAlreadyPurchased";

	public AlreadyPurchasedException() {
		super(errorMessage(ERROR_PURCHASE_ALREADY_PURCHASED));
	}
}
