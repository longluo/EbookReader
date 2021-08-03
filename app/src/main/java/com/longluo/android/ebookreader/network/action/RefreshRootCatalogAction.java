package com.longluo.android.ebookreader.network.action;

import android.app.Activity;

import com.longluo.ebookreader.network.NetworkTree;
import com.longluo.ebookreader.network.NetworkLibrary;

import com.longluo.ebookreader.R;

import com.longluo.android.ebookreader.network.NetworkLibraryActivity;

public class RefreshRootCatalogAction extends RootAction {
	public RefreshRootCatalogAction(NetworkLibraryActivity activity) {
		super(activity, ActionCode.REFRESH, "refreshCatalogsList", R.drawable.ic_menu_refresh);
	}

	@Override
	public boolean isEnabled(NetworkTree tree) {
		return !myLibrary.isUpdateInProgress();
	}

	@Override
	public void run(NetworkTree tree) {
		myLibrary.runBackgroundUpdate(true);
		((NetworkLibraryActivity)myActivity).requestCatalogPlugins();
	}
}
