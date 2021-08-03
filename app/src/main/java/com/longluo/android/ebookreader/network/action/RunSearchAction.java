package com.longluo.android.ebookreader.network.action;

import android.app.Activity;
import android.os.Bundle;

import com.longluo.ebookreader.tree.FBTree;
import com.longluo.ebookreader.network.NetworkLibrary;
import com.longluo.ebookreader.network.NetworkTree;
import com.longluo.ebookreader.network.tree.SearchCatalogTree;

import com.longluo.ebookreader.R;

import com.longluo.android.ebookreader.network.NetworkLibraryActivity;
import com.longluo.android.ebookreader.network.NetworkSearchActivity;

import com.longluo.android.util.DeviceType;
import com.longluo.android.util.SearchDialogUtil;

public class RunSearchAction extends Action {
	public static SearchCatalogTree getSearchTree(FBTree tree) {
		for (; tree != null; tree = tree.Parent) {
			for (FBTree t : tree.subtrees()) {
				if (t instanceof SearchCatalogTree) {
					return (SearchCatalogTree)t;
				}
			}
		}
		return null;
	}

	private final boolean myFromContextMenu;

	public RunSearchAction(Activity activity, boolean fromContextMenu) {
		super(activity, ActionCode.SEARCH, "networkSearch", R.drawable.ic_menu_search);
		myFromContextMenu = fromContextMenu;
	}

	@Override
	public boolean isVisible(NetworkTree tree) {
		if (myFromContextMenu) {
			return tree instanceof SearchCatalogTree;
		} else {
			return getSearchTree(tree) != null;
		}
	}

	@Override
	public boolean isEnabled(NetworkTree tree) {
		return myLibrary.getStoredLoader(getSearchTree(tree)) == null;
	}

	@Override
	public void run(NetworkTree tree) {
		final Bundle bundle = new Bundle();
		bundle.putSerializable(
			NetworkLibraryActivity.TREE_KEY_KEY,
			getSearchTree(tree).getUniqueKey()
		);
		final NetworkLibrary library = myLibrary;
		if (DeviceType.Instance().hasStandardSearchDialog()) {
			myActivity.startSearch(library.NetworkSearchPatternOption.getValue(), true, bundle, false);
		} else {
			SearchDialogUtil.showDialog(myActivity, NetworkSearchActivity.class, library.NetworkSearchPatternOption.getValue(), null, bundle);
		}
	}
}
