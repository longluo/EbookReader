package com.longluo.android.ebookreader.network.action;

import com.longluo.zlibrary.core.network.ZLNetworkContext;
import com.longluo.ebookreader.R;

import com.longluo.ebookreader.network.*;
import com.longluo.ebookreader.network.tree.NetworkCatalogTree;
import com.longluo.ebookreader.network.urlInfo.UrlInfo;

import com.longluo.android.ebookreader.network.NetworkLibraryActivity;

public class ReloadCatalogAction extends CatalogAction {
	private final ZLNetworkContext myNetworkContext;

	public ReloadCatalogAction(NetworkLibraryActivity activity, ZLNetworkContext nc) {
		super(activity, ActionCode.RELOAD_CATALOG, "reload", R.drawable.ic_menu_refresh);
		myNetworkContext = nc;
	}

	@Override
	public boolean isVisible(NetworkTree tree) {
		if (!super.isVisible(tree)) {
			return false;
		}
		final NetworkCatalogItem item = ((NetworkCatalogTree)tree).Item;
		if (!(item instanceof NetworkURLCatalogItem)) {
			return false;
		}
		return ((NetworkURLCatalogItem)item).getUrl(UrlInfo.Type.Catalog) != null;
	}

	@Override
	public boolean isEnabled(NetworkTree tree) {
		return myLibrary.getStoredLoader(tree) == null;
	}

	@Override
	public void run(NetworkTree tree) {
		if (myLibrary.getStoredLoader(tree) != null) {
			return;
		}
		((NetworkCatalogTree)tree).clearCatalog();
		((NetworkCatalogTree)tree).startItemsLoader(myNetworkContext, false, false);
	}
}
