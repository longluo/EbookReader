package com.longluo.ebookreader.network.tree;

import com.longluo.ebookreader.network.*;

public class ManageCatalogsItemTree extends NetworkTree {
	public ManageCatalogsItemTree(NetworkTree parent) {
		super(parent);
	}

	@Override
	public String getName() {
		return NetworkLibrary.resource().getResource("manageCatalogs").getValue();
	}

	@Override
	public String getSummary() {
		return NetworkLibrary.resource().getResource("manageCatalogs").getResource("summary").getValue();
	}

	@Override
	protected String getStringId() {
		return "@ManageCatalogs";
	}
}
