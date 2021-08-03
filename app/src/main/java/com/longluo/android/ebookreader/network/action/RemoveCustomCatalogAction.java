package com.longluo.android.ebookreader.network.action;

import android.app.Activity;

import com.longluo.ebookreader.network.NetworkLibrary;
import com.longluo.ebookreader.network.NetworkTree;
import com.longluo.ebookreader.network.ICustomNetworkLink;
import com.longluo.ebookreader.network.tree.NetworkCatalogRootTree;

public class RemoveCustomCatalogAction extends CatalogAction {
	public RemoveCustomCatalogAction(Activity activity) {
		super(activity, ActionCode.CUSTOM_CATALOG_REMOVE, "removeCustomCatalog");
	}

	@Override
	public boolean isVisible(NetworkTree tree) {
		return
			tree instanceof NetworkCatalogRootTree &&
			tree.getLink() instanceof ICustomNetworkLink;
	}

	@Override
	public void run(NetworkTree tree) {
		myLibrary.removeCustomLink((ICustomNetworkLink)tree.getLink());
		myLibrary.synchronize();
	}
}
