package com.longluo.ebookreader.network;

import com.longluo.zlibrary.core.network.ZLNetworkException;

public abstract class NetworkException extends ZLNetworkException {
	private static final long serialVersionUID = 8931535868304063605L;

	public static final String ERROR_INTERNAL = "internalError";
	public static final String ERROR_PURCHASE_NOT_ENOUGH_MONEY = "purchaseNotEnoughMoney";
	public static final String ERROR_PURCHASE_MISSING_BOOK = "purchaseMissingBook";
	public static final String ERROR_BOOK_NOT_PURCHASED = "bookNotPurchased";
	public static final String ERROR_DOWNLOAD_LIMIT_EXCEEDED = "downloadLimitExceeded";

	public static final String ERROR_EMAIL_NOT_SPECIFIED = "emailNotSpecified";

	public static final String ERROR_NO_USER_FOR_EMAIL = "noUserForEmail";

	public static final String ERROR_UNSUPPORTED_OPERATION = "unsupportedOperation";

	public static final String ERROR_NOT_AN_OPDS = "notAnOPDS";
	public static final String ERROR_NO_REQUIRED_INFORMATION = "noRequiredInformation";
	public static final String ERROR_CACHE_DIRECTORY_ERROR = "cacheDirectoryError";

	private NetworkException() {
		super(null);
	}
}
