package com.longluo.android.ebookreader.network.action;

import android.app.Activity;

import com.longluo.ebookreader.network.*;
import com.longluo.ebookreader.network.tree.NetworkCatalogRootTree;

public class DisableCatalogAction extends Action {
	public DisableCatalogAction(Activity activity) {
		super(activity, ActionCode.DISABLE_CATALOG, "disableCatalog", -1);
	}

	@Override
	public boolean isVisible(NetworkTree tree) {
		return
			tree instanceof NetworkCatalogRootTree &&
			tree.getLink().getType() != INetworkLink.Type.Sync;
	}

	@Override
	public void run(NetworkTree tree) {
		myLibrary.setLinkActive(tree.getLink(), false);
		myLibrary.synchronize();
		// TODO: invalidate view
	}
}
