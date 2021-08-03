package com.longluo.android.ebookreader.network.action;

import android.app.Activity;

import com.longluo.ebookreader.network.NetworkTree;
import com.longluo.ebookreader.network.tree.NetworkCatalogRootTree;
import com.longluo.ebookreader.network.authentication.NetworkAuthenticationManager;

import com.longluo.android.ebookreader.network.Util;

public class SignInAction extends Action {
	public SignInAction(Activity activity) {
		super(activity, ActionCode.SIGNIN, "signIn", -1);
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
		Util.runAuthenticationDialog(myActivity, tree.getLink(), null);
	}
}
