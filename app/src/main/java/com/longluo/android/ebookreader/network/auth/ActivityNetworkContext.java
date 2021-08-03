package com.longluo.android.ebookreader.network.auth;

import java.net.URI;
import java.util.*;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.longluo.zlibrary.core.network.*;
import com.longluo.android.ebookreader.network.NetworkLibraryActivity;
import com.longluo.android.util.OrientationUtil;

public final class ActivityNetworkContext extends AndroidNetworkContext {
	private final Activity myActivity;
	private volatile boolean myAuthorizationConfirmed;

	private volatile String myAccountName;

	public ActivityNetworkContext(Activity activity) {
		myActivity = activity;
	}

	public Context getContext() {
		return myActivity;
	}

	public synchronized void onResume() {
		notifyAll();
	}

	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		boolean processed = true;
		try {
			switch (requestCode) {
				default:
					processed = false;
					break;
				case NetworkLibraryActivity.REQUEST_ACCOUNT_PICKER:
					if (resultCode == Activity.RESULT_OK && data != null) {
						myAccountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
					} else {
						myAccountName = null;
					}
					break;
				case NetworkLibraryActivity.REQUEST_AUTHORISATION:
					if (resultCode == Activity.RESULT_OK) {
						myAuthorizationConfirmed = true;
					}
					break;
				case NetworkLibraryActivity.REQUEST_WEB_AUTHORISATION_SCREEN:
					cookieStore().reset();
					break;
			}
		} finally {
			if (processed) {
				synchronized (this) {
					notifyAll();
				}
			}
			return processed;
		}
	}

	@Override
	protected Map<String,String> authenticateWeb(URI uri, String realm, String authUrl, String completeUrl, String verificationUrl) {
		System.err.println("+++ WEB AUTH +++");
		final Intent intent = new Intent(myActivity, WebAuthorisationScreen.class);
		intent.setData(Uri.parse(authUrl));
		intent.putExtra(WebAuthorisationScreen.COMPLETE_URL_KEY, completeUrl);
		startActivityAndWait(intent, NetworkLibraryActivity.REQUEST_WEB_AUTHORISATION_SCREEN);
		System.err.println("--- WEB AUTH ---");
		return verify(verificationUrl);
	}

	private void startActivityAndWait(Intent intent, int requestCode) {
		synchronized (this) {
			OrientationUtil.startActivityForResult(myActivity, intent, requestCode);
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
	}
}
