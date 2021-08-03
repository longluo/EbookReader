package com.longluo.ebookreader.network.tree;

import com.longluo.util.Pair;

import com.longluo.ebookreader.tree.FBTree;
import com.longluo.ebookreader.network.*;

public class NetworkCatalogRootTree extends NetworkCatalogTree {
	public NetworkCatalogRootTree(RootTree parent, INetworkLink link, int position) {
		super(parent, link, (NetworkCatalogItem)link.libraryItem(), position);
	}

	public NetworkCatalogRootTree(RootTree parent, INetworkLink link) {
		this(parent, link, -1);
	}

	@Override
	public Pair<String,String> getTreeTitle() {
		return new Pair(getName(), null);
	}

	@Override
	protected void addSpecialTrees() {
		super.addSpecialTrees();
		final BasketItem basketItem = getLink().getBasketItem();
		if (basketItem != null) {
			myChildrenItems.add(basketItem);
			new BasketCatalogTree(this, basketItem, -1);
		}
	}

	@Override
	public int compareTo(FBTree tree) {
		if (!(tree instanceof NetworkCatalogRootTree)) {
			return 1;
		}
		return getLink().compareTo(((NetworkCatalogRootTree)tree).getLink());
	}
}
