package com.longluo.android.ebookreader.network.action;

import android.app.Activity;

import com.longluo.zlibrary.core.money.Money;

import com.longluo.ebookreader.network.INetworkLink;
import com.longluo.ebookreader.network.NetworkTree;
import com.longluo.ebookreader.network.tree.NetworkCatalogRootTree;
import com.longluo.ebookreader.network.tree.TopUpTree;
import com.longluo.ebookreader.network.authentication.NetworkAuthenticationManager;

import com.longluo.android.ebookreader.network.TopupMenuActivity;

public class TopupAction extends Action {
	public TopupAction(Activity activity) {
		super(activity, ActionCode.TOPUP, "topup", -1);
	}

	@Override
	public boolean isVisible(NetworkTree tree) {
		if (tree instanceof TopUpTree) {
			return true;
		} else if (tree instanceof NetworkCatalogRootTree) {
			final INetworkLink link = tree.getLink();
			final NetworkAuthenticationManager mgr = link.authenticationManager();
			return
				mgr != null &&
				mgr.mayBeAuthorised(false) &&
				mgr.currentAccount() != null &&
				TopupMenuActivity.isTopupSupported(link);
		} else {
			return false;
		}
	}

	@Override
	public void run(NetworkTree tree) {
		final INetworkLink link = tree.getLink();
		if (link != null) {
			TopupMenuActivity.runMenu(myActivity, link, null);
		}
	}

	@Override
	public String getContextLabel(NetworkTree tree) {
		final INetworkLink link = tree.getLink();
		Money account = null;
		if (link != null) {
			final NetworkAuthenticationManager mgr = link.authenticationManager();
			if (mgr != null && mgr.mayBeAuthorised(false)) {
				account = mgr.currentAccount();
			}
		}
		return super.getContextLabel(tree).replace("%s", account != null ? account.toString() : "");
	}
}
