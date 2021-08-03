package com.longluo.android.ebookreader.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.longluo.zlibrary.core.network.ZLNetworkException;

import com.longluo.ebookreader.network.*;
import com.longluo.ebookreader.network.authentication.NetworkAuthenticationManager;
import com.longluo.ebookreader.network.authentication.litres.LitResAuthenticationManager;

public class ListenerCallback extends BroadcastReceiver implements UserRegistrationConstants {
	@Override
	public void onReceive(Context context, final Intent intent) {
		final NetworkLibrary library = Util.networkLibrary(context);

		if (Util.SIGNIN_ACTION.equals(intent.getAction())) {
			final String url = intent.getStringExtra(CATALOG_URL);
			final INetworkLink link = library.getLinkByUrl(url);
			if (link != null) {
				final NetworkAuthenticationManager mgr = link.authenticationManager();
				if (mgr instanceof LitResAuthenticationManager) {
					new Thread(new Runnable() {
						public void run() {
							try {
								processSignup((LitResAuthenticationManager)mgr, intent);
							} catch (ZLNetworkException e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
			}
		} else {
			library.fireModelChangedEvent(NetworkLibrary.ChangeListener.Code.SomeCode);
		}
	}

	private static void processSignup(LitResAuthenticationManager mgr, Intent data) throws ZLNetworkException {
		mgr.initUser(
			data.getStringExtra(USER_REGISTRATION_USERNAME),
			data.getStringExtra(USER_REGISTRATION_LITRES_SID),
			"",
			false
		);
		//if (!mgr.isAuthorised(true)) {
		//	throw new ZLNetworkException(NetworkException.ERROR_AUTHENTICATION_FAILED);
		//}
		try {
			mgr.authorise(
				data.getStringExtra(USER_REGISTRATION_USERNAME),
				data.getStringExtra(USER_REGISTRATION_PASSWORD)
			);
			mgr.initialize();
		} catch (ZLNetworkException e) {
			mgr.logOut();
			throw e;
		}
	}
}