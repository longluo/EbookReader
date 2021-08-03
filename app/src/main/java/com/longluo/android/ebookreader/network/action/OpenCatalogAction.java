package com.longluo.android.ebookreader.network.action;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.longluo.zlibrary.core.network.ZLNetworkContext;

import com.longluo.ebookreader.network.*;
import com.longluo.ebookreader.network.tree.*;

import com.longluo.android.ebookreader.network.*;
import com.longluo.android.util.OrientationUtil;
import com.longluo.android.util.PackageUtil;

public class OpenCatalogAction extends Action {
	private final ZLNetworkContext myNetworkContext;

	public OpenCatalogAction(Activity activity, ZLNetworkContext nc) {
		super(activity, ActionCode.OPEN_CATALOG, "openCatalog", -1);
		myNetworkContext = nc;
	}

	@Override
	public boolean isVisible(NetworkTree tree) {
		if (tree instanceof NetworkAuthorTree || tree instanceof NetworkSeriesTree) {
			return true;
		} else if (tree instanceof NetworkCatalogTree) {
			return ((NetworkCatalogTree)tree).canBeOpened();
		} else {
			return false;
		}
	}

	@Override
	public void run(NetworkTree tree) {
		if (tree instanceof NetworkCatalogTree) {
			doExpandCatalog((NetworkCatalogTree)tree);
		} else {
			doOpenTree(tree);
		}
	}

	private void doOpenTree(NetworkTree tree) {
		if (myActivity instanceof NetworkLibraryActivity) {
			((NetworkLibraryActivity)myActivity).openTree(tree);
		} else {
			OrientationUtil.startActivity(
				myActivity,
				new Intent(myActivity.getApplicationContext(), NetworkLibrarySecondaryActivity.class)
					.putExtra(NetworkLibraryActivity.TREE_KEY_KEY, tree.getUniqueKey())
			);
		}
	}

	private void doExpandCatalog(final NetworkCatalogTree tree) {
		final NetworkItemsLoader loader = myLibrary.getStoredLoader(tree);
		if (loader != null && loader.canResumeLoading()) {
			doOpenTree(tree);
		} else if (loader != null) {
			loader.setPostRunnable(new Runnable() {
				public void run() {
					doLoadCatalog(tree);
				}
			});
		} else {
			doLoadCatalog(tree);
		}
	}

	private void doLoadCatalog(final NetworkCatalogTree tree) {
		boolean resumeNotLoad = false;
		if (tree.hasChildren()) {
			if (tree.isContentValid()) {
				if (tree.Item.supportsResumeLoading()) {
					resumeNotLoad = true;
				} else {
					doOpenTree(tree);
					return;
				}
			} else {
				tree.clearCatalog();
			}
		}

		tree.startItemsLoader(myNetworkContext, true, resumeNotLoad);
		doOpenTree(tree);
	}
}
