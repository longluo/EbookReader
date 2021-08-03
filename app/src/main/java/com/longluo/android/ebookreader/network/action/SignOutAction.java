package com.longluo.android.ebookreader.network.action;

import android.app.Activity;

import com.longluo.zlibrary.core.network.ZLNetworkContext;

import com.longluo.ebookreader.network.*;
import com.longluo.ebookreader.network.authentication.NetworkAuthenticationManager;
import com.longluo.ebookreader.network.sync.SyncUtil;
import com.longluo.ebookreader.network.tree.NetworkCatalogRootTree;

import com.longluo.android.util.UIUtil;

public class SignOutAction extends Action {
	private final ZLNetworkContext myNetworkContext;

	public SignOutAction(Activity activity, ZLNetworkContext context) {
		super(activity, ActionCode.SIGNOUT, "signOut", -1);
		myNetworkContext = context;
	}

	@Override
	public boolean isVisible(NetworkTree tree) {
		if (!(tree instanceof NetworkCatalogRootTree)) {
			return false;
		}

		final INetworkLink link = tree.getLink();
		if (link instanceof ISyncNetworkLink) {
			return ((ISyncNetworkLink)link).isLoggedIn(myNetworkContext);
		}

		final NetworkAuthenticationManager mgr = link.authenticationManager();
		return mgr != null && mgr.mayBeAuthorised(false);
	}

	@Override
	public void run(NetworkTree tree) {
		final INetworkLink link = tree.getLink();
		if (link instanceof ISyncNetworkLink) {
			((ISyncNetworkLink)link).logout(myNetworkContext);
			((NetworkCatalogRootTree)tree).clearCatalog();
			return;
		}

		final NetworkAuthenticationManager mgr = link.authenticationManager();
		final Runnable runnable = new Runnable() {
			public void run() {
				if (mgr.mayBeAuthorised(false)) {
					mgr.logOut();
					myActivity.runOnUiThread(new Runnable() {
						public void run() {
							myLibrary.invalidateVisibility();
							myLibrary.synchronize();
						}
					});
				}
			}
		};
		UIUtil.wait("signOut", runnable, myActivity);
	}

	private String accountName(NetworkTree tree) {
		final INetworkLink link = tree.getLink();
		if (link instanceof ISyncNetworkLink) {
			return SyncUtil.getAccountName(myNetworkContext);
		}

		final NetworkAuthenticationManager mgr = link.authenticationManager();
		return mgr != null && mgr.mayBeAuthorised(false) ? mgr.getVisibleUserName() : null;
	}

	@Override
	public String getOptionsLabel(NetworkTree tree) {
		final String account = accountName(tree);
		return super.getOptionsLabel(tree).replace("%s", account != null ? account : "");
	}

	@Override
	public String getContextLabel(NetworkTree tree) {
		final String account = accountName(tree);
		return super.getContextLabel(tree).replace("%s", account != null ? account : "");
	}
}
