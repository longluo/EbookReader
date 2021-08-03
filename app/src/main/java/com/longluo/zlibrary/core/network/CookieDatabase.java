package com.longluo.zlibrary.core.network;

import java.util.List;
import java.util.Date;

import org.apache.http.cookie.Cookie;

public abstract class CookieDatabase {
	private static CookieDatabase ourInstance;

	public static CookieDatabase getInstance() {
		return ourInstance;
	}

	protected CookieDatabase() {
		ourInstance = this;
	}

	protected abstract void removeObsolete(Date date);
	protected abstract void removeAll();
	protected abstract void removeForDomain(String domain);
	protected abstract void saveCookies(List<Cookie> cookies);
	protected abstract List<Cookie> loadCookies();
}
