package com.longluo.android.ebookreader.network.action;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.longluo.ebookreader.network.NetworkTree;
import com.longluo.ebookreader.network.tree.RootTree;
import com.longluo.ebookreader.network.tree.AddCustomCatalogItemTree;

import com.longluo.android.ebookreader.network.AddCatalogMenuActivity;

import com.longluo.ebookreader.R;

public class AddCustomCatalogAction extends Action {
	public AddCustomCatalogAction(Activity activity) {
		super(activity, ActionCode.CUSTOM_CATALOG_ADD, "addCustomCatalog", R.drawable.ic_menu_add);
	}

	@Override
	public boolean isVisible(NetworkTree tree) {
		return tree instanceof RootTree || tree instanceof AddCustomCatalogItemTree;
	}

	@Override
	public void run(NetworkTree tree) {
		myActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://data.fbreader.org/add_catalog"), myActivity, AddCatalogMenuActivity.class));
	}
}
