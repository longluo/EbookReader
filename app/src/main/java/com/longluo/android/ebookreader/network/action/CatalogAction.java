package com.longluo.android.ebookreader.network.action;

import android.app.Activity;

import com.longluo.ebookreader.network.NetworkTree;
import com.longluo.ebookreader.network.tree.NetworkCatalogTree;

abstract class CatalogAction extends Action {
	protected CatalogAction(Activity activity, int code, String resourceKey, int iconId) {
		super(activity, code, resourceKey, iconId);
	}

	protected CatalogAction(Activity activity, int code, String resourceKey) {
		super(activity, code, resourceKey, -1);
	}

	@Override
	public boolean isVisible(NetworkTree tree) {
		return tree instanceof NetworkCatalogTree;
	}
}
