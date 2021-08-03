package com.longluo.zlibrary.core.network;

import com.longluo.zlibrary.core.resources.ZLResource;

public class ZLNetworkException extends Exception {
	private static final long serialVersionUID = 4272384299121648643L;

	// Messages with no parameters:
	public static final String ERROR_UNKNOWN_ERROR = "unknownErrorMessage";
	public static final String ERROR_TIMEOUT = "operationTimedOutMessage";
	public static final String ERROR_UNSUPPORTED_PROTOCOL = "unsupportedProtocol";
	public static final String ERROR_INVALID_URL = "invalidURL";

	// Messages with one parameter:
	public static final String ERROR_SOMETHING_WRONG = "somethingWrongMessage";
	public static final String ERROR_CREATE_DIRECTORY = "couldntCreateDirectoryMessage";
	public static final String ERROR_CREATE_FILE = "couldntCreateFileMessage";
	public static final String ERROR_CONNECT_TO_HOST = "couldntConnectMessage";
	public static final String ERROR_RESOLVE_HOST = "couldntResolveHostMessage";
	public static final String ERROR_HOST_CANNOT_BE_REACHED = "hostCantBeReached";
	public static final String ERROR_CONNECTION_REFUSED = "connectionRefused";

	private static ZLResource getResource() {
		return ZLResource.resource("dialog").getResource("networkError");
	}

	protected static String errorMessage(String code) {
		return code != null ? getResource().getResource(code).getValue() : "null";
	}

	public static ZLNetworkException forCode(String code, String arg, Throwable cause) {
		final String message;
		if (code == null) {
			message = "null";
		} else {
			if (arg == null) {
				arg = "null";
			}
			message = getResource().getResource(code).getValue().replace("%s", arg);
		}
		return new ZLNetworkException(message, cause);
	}

	public static ZLNetworkException forCode(String code, Throwable cause) {
		return new ZLNetworkException(errorMessage(code), cause);
	}

	public static ZLNetworkException forCode(String code, String arg) {
		return forCode(code, arg, null);
	}

	public static ZLNetworkException forCode(String code) {
		return forCode(code, (Throwable)null);
	}

	public ZLNetworkException(String message) {
		super(message);
	}

	public ZLNetworkException(String message, Throwable cause) {
		super(message, cause);
	}
}
