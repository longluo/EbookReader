package com.longluo.ebookreader.network.tree;

import com.longluo.ebookreader.network.RecentCatalogListItem;

public class RecentCatalogListTree extends NetworkCatalogTree {
	public RecentCatalogListTree(RootTree parent, RecentCatalogListItem item) {
		super(parent, null, item, -1);
	}
}
