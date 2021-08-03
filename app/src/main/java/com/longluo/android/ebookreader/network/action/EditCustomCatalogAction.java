package com.longluo.android.ebookreader.network.action;

import android.app.Activity;
import android.content.Intent;

import com.longluo.ebookreader.network.NetworkTree;
import com.longluo.ebookreader.network.ICustomNetworkLink;
import com.longluo.ebookreader.network.tree.NetworkCatalogRootTree;

import com.longluo.android.ebookreader.network.Util;
import com.longluo.android.ebookreader.network.AddCustomCatalogActivity;

public class EditCustomCatalogAction extends CatalogAction {
	public EditCustomCatalogAction(Activity activity) {
		super(activity, ActionCode.CUSTOM_CATALOG_EDIT, "editCustomCatalog");
	}

	@Override
	public boolean isVisible(NetworkTree tree) {
		return
			tree instanceof NetworkCatalogRootTree &&
			tree.getLink() instanceof ICustomNetworkLink;
	}

	@Override
	public void run(NetworkTree tree) {
		final Intent intent = new Intent(myActivity, AddCustomCatalogActivity.class);
		Util.intentByLink(intent, tree.getLink());
		intent.setAction(Util.EDIT_CATALOG_ACTION);
		myActivity.startActivity(intent);
	}
}
