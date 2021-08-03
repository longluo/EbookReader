package com.longluo.android.ebookreader.network.auth;

import java.util.*;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.Window;
import android.webkit.*;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie2;

import com.longluo.zlibrary.core.network.ZLNetworkManager;
import com.longluo.zlibrary.ui.android.network.SQLiteCookieDatabase;

import com.longluo.android.util.OrientationUtil;

public class WebAuthorisationScreen extends Activity {
	public static final String COMPLETE_URL_KEY = "android.fbreader.data.complete.url";

	private final ActivityNetworkContext myNetworkContext = new ActivityNetworkContext(this);

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		requestWindowFeature(Window.FEATURE_PROGRESS);
		SQLiteCookieDatabase.init(this);
		CookieSyncManager.createInstance(getApplicationContext());
		CookieManager.getInstance().removeAllCookie();
		final Intent intent = getIntent();
		final Uri data = intent.getData();
		if (data == null || data.getHost() == null) {
			finish();
			return;
		}
		final String completeUrl = intent.getStringExtra(COMPLETE_URL_KEY);

		OrientationUtil.setOrientation(this, intent);
		final WebView view = new WebView(this);
		view.getSettings().setJavaScriptEnabled(true);

		view.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {
				setProgress(progress * 100);
			}
		});
		view.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				setTitle(url);
				if (url != null && url.startsWith(completeUrl)) {
					final HashMap<String,String> cookies = new HashMap<String,String>();
					final String cookieString = CookieManager.getInstance().getCookie(url);
					if (cookieString != null) {
						// cookieString is a string like NAME=VALUE [; NAME=VALUE]
						for (String pair : cookieString.split(";")) {
							final String[] parts = pair.split("=", 2);
							if (parts.length != 2) {
								continue;
							}
							cookies.put(parts[0].trim(), parts[1].trim());
						}
					}
					storeCookies(data.getHost(), cookies);
					WebAuthorisationScreen.this.setResult(RESULT_OK);
					finish();
				}
			}

			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.ECLAIR_MR1) {
					// hack for auth problem in android 2.1
					handler.proceed();
				} else {
					super.onReceivedSslError(view, handler, error);
				}
			}
		});
		setContentView(view);
		view.loadUrl(intent.getDataString());
	}

	private void storeCookies(String host, Map<String,String> cookies) {
		final ZLNetworkManager.CookieStore store = myNetworkContext.cookieStore();

		for (Map.Entry<String,String> entry : cookies.entrySet()) {
			final BasicClientCookie2 c =
				new BasicClientCookie2(entry.getKey(), entry.getValue());
			c.setDomain(host);
			c.setPath("/");
			final Calendar expire = Calendar.getInstance();
			expire.add(Calendar.YEAR, 1);
			c.setExpiryDate(expire.getTime());
			c.setSecure(true);
			c.setDiscard(false);
			store.addCookie(c);
		}
	}
}
