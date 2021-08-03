package com.longluo.android.ebookreader.network;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.network.NetworkLibrary;

import com.longluo.android.ebookreader.api.FBReaderIntents;
import com.longluo.android.ebookreader.api.PluginApi;

public class AddCatalogMenuActivity extends MenuActivity {
	private final ZLResource myResource = NetworkLibrary.resource().getResource("addCatalog");

	private void addItem(String id, int weight) {
		myInfos.add(new PluginApi.MenuActionInfo(
			Uri.parse("http://data.fbreader.org/add_catalog/" + id),
			myResource.getResource(id).getValue(),
			weight
		));
	}

	@Override
	protected void init() {
		setTitle(myResource.getResource("title").getValue());
		addItem("editUrl", 1);
		//addItem("scanLocalNetwork", 2);
	}

	@Override
	protected String getAction() {
		return Util.ADD_CATALOG_ACTION;
	}

	@Override
	protected void runItem(final PluginApi.MenuActionInfo info) {
		try {
			startActivity(
				new Intent(getAction()).addCategory(Intent.CATEGORY_DEFAULT).setData(info.getId())
			);
		} catch (ActivityNotFoundException e) {
		}
		finish();
	}
}
