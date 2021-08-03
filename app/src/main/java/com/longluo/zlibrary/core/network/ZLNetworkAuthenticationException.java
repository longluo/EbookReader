package com.longluo.zlibrary.core.network;

import com.longluo.zlibrary.core.resources.ZLResource;

public class ZLNetworkAuthenticationException extends ZLNetworkException {
	public static final String ERROR_AUTHENTICATION_FAILED = "authenticationFailed";

	public ZLNetworkAuthenticationException() {
		super(errorMessage(ERROR_AUTHENTICATION_FAILED));
	}

	public ZLNetworkAuthenticationException(String message) {
		super(message);
	}

	public ZLNetworkAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
}
