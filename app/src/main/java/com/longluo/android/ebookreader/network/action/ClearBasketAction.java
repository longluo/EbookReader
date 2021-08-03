package com.longluo.android.ebookreader.network.action;

import android.app.Activity;

import com.longluo.ebookreader.network.NetworkTree;
import com.longluo.ebookreader.network.BasketItem;
import com.longluo.ebookreader.network.tree.BasketCatalogTree;

public class ClearBasketAction extends CatalogAction {
	public ClearBasketAction(Activity activity) {
		super(activity, ActionCode.BASKET_CLEAR, "clearBasket");
	}

	@Override
	public boolean isVisible(NetworkTree tree) {
		return tree instanceof BasketCatalogTree && ((BasketCatalogTree)tree).canBeOpened();
	}

	@Override
	public void run(NetworkTree tree) {
		((BasketItem)((BasketCatalogTree)tree).Item).clear();
	}
}
