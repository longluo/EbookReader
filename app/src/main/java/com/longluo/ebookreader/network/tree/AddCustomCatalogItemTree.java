package com.longluo.ebookreader.network.tree;

import com.longluo.ebookreader.network.*;

public class AddCustomCatalogItemTree extends NetworkTree {
	public AddCustomCatalogItemTree(NetworkTree parent) {
		super(parent);
	}

	@Override
	public String getName() {
		return NetworkLibrary.resource().getResource("addCustomCatalog").getValue();
	}

	@Override
	public String getSummary() {
		return NetworkLibrary.resource().getResource("addCustomCatalogSummary").getValue();
	}

	@Override
	protected String getStringId() {
		return "@Add Custom Catalog";
	}
}
