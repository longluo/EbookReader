package com.longluo.android.ebookreader.network.auth;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.longluo.zlibrary.core.network.*;

public abstract class AndroidNetworkContext extends ZLNetworkContext {
	private volatile ConnectivityManager myConnectivityManager;

	@Override
	public Map<String,String> authenticate(URI uri, String realm, Map<String,String> params) {
		if (!"https".equalsIgnoreCase(uri.getScheme())) {
			return errorMap("Connection is not secure");
		}

		String authUrl = null;
		final String account = getAccountName(uri.getHost(), realm);
		if (account != null) {
			final String urlWithAccount = params.get("auth-url-web-with-email");
			if (urlWithAccount != null) {
				authUrl = url(uri, urlWithAccount.replace("{email}", account));
			}
		} else {
			authUrl = url(uri, params, "auth-url-web");
		}
		final String completeUrl = url(uri, params, "complete-url-web");
		final String verificationUrl = url(uri, params, "verification-url");
		if (authUrl == null || completeUrl == null || verificationUrl == null) {
			return errorMap("No data for web authentication");
		}
		return authenticateWeb(uri, realm, authUrl, completeUrl, verificationUrl);
	}

	protected abstract Context getContext();
	protected abstract Map<String,String> authenticateWeb(URI uri, String realm, String authUrl, String completeUrl, String verificationUrl);

	protected Map<String,String> errorMap(String message) {
		return Collections.singletonMap("error", message);
	}

	protected Map<String,String> errorMap(Throwable exception) {
		final String message = exception.getMessage();
		return errorMap(message != null ? message : exception.getClass().getName());
	}

	protected Map<String,String> verify(final String verificationUrl) {
		final Map<String,String> result = new HashMap<String,String>();
		performQuietly(new JsonRequest(verificationUrl) {
			public void processResponse(Object response) {
				result.putAll((Map)response);
			}
		});
		return result;
	}

	protected String url(URI base, Map<String,String> params, String key) {
		return url(base, params.get(key));
	}

	protected String url(URI base, String path) {
		if (path == null) {
			return null;
		}
		try {
			final URI relative = new URI(path);
			return relative.isAbsolute() ? null : base.resolve(relative).toString();
		} catch (URISyntaxException e) {
			return null;
		}
	}

	protected final NetworkInfo getActiveNetworkInfo() {
		if (myConnectivityManager == null) {
			myConnectivityManager =
				(ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		return myConnectivityManager != null ? myConnectivityManager.getActiveNetworkInfo() : null;
	}

	@Override
	protected void perform(ZLNetworkRequest request, int socketTimeout, int connectionTimeout) throws ZLNetworkException {
		final NetworkInfo info = getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			throw ZLNetworkException.forCode("networkNotAvailable");
		}
		
		super.perform(request, socketTimeout, connectionTimeout);
	}
}
