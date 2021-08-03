package com.longluo.android.ebookreader.network.action;

import android.app.Activity;

import com.longluo.ebookreader.network.NetworkLibrary;
import com.longluo.ebookreader.network.NetworkTree;
import com.longluo.ebookreader.network.tree.RootTree;

import com.longluo.android.ebookreader.network.NetworkLibraryActivity;

public class OpenRootAction extends Action {
	public OpenRootAction(Activity activity) {
		super(activity, ActionCode.OPEN_ROOT, "openRoot", -1);
	}

	@Override
	public boolean isVisible(NetworkTree tree) {
		if (!(myActivity instanceof NetworkLibraryActivity)) {
			return false;
		}
		if (tree instanceof RootTree) {
			return false;
		}
		for (; tree != null; tree = (NetworkTree)tree.Parent) {
			if (tree instanceof RootTree) {
				return tree == myLibrary.getRootTree();
			}
		}
		return false;
	}

	@Override
	public void run(NetworkTree tree) {
		final NetworkLibraryActivity activity = (NetworkLibraryActivity)myActivity;
		activity.openTree(myLibrary.getRootTree());
		activity.clearHistory();
	}
}
