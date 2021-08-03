package com.longluo.android.ebookreader.network.action;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.net.Uri;

import com.longluo.ebookreader.network.INetworkLink;
import com.longluo.ebookreader.network.NetworkTree;
import com.longluo.ebookreader.network.authentication.NetworkAuthenticationManager;
import com.longluo.ebookreader.network.tree.NetworkCatalogRootTree;
import com.longluo.ebookreader.network.urlInfo.UrlInfo;

import com.longluo.android.ebookreader.network.Util;
import com.longluo.android.ebookreader.network.litres.UserRegistrationActivity;

public class SignUpAction extends Action {
	public SignUpAction(Activity activity) {
		super(activity, ActionCode.SIGNUP, "signUp", -1);
	}

	@Override
	public boolean isVisible(NetworkTree tree) {
		if (!(tree instanceof NetworkCatalogRootTree)) {
			return false;
		}

		final NetworkAuthenticationManager mgr = tree.getLink().authenticationManager();
		return mgr != null && !mgr.mayBeAuthorised(false);
	}

	@Override
	public void run(NetworkTree tree) {
		final INetworkLink link = tree.getLink();
		try {
			myActivity.startActivity(Util.authorisationIntent(
				link, myActivity, UserRegistrationActivity.class
			));
		} catch (ActivityNotFoundException e) {
		}
	}
}
