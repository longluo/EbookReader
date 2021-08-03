/*
 * This code is in the public domain.
 */package com.longluo.android.ebookreader.api;

public class ApiException extends Exception {
	private static final long serialVersionUID = -6316637693779831867L;

	ApiException(String message) {
		super(message);
	}

	ApiException(Exception parent) {
		super(parent);
	}
}
