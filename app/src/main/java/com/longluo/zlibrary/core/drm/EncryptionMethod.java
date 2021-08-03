package com.longluo.zlibrary.core.drm;

public abstract class EncryptionMethod {
	public static final String UNSUPPORTED = "unsupported";
	public static final String EMBEDDING = "embedding";
	public static final String MARLIN = "marlin";
	public static final String KINDLE = "kindle";

	public static boolean isSupported(String method) {
		return EMBEDDING.equals(method);
	}
}
