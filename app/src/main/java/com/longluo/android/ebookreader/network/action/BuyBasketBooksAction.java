package com.longluo.android.ebookreader.network.action;

import java.util.*;

import android.app.Activity;

import com.longluo.ebookreader.network.*;
import com.longluo.ebookreader.network.tree.*;
import com.longluo.ebookreader.tree.FBTree;

import com.longluo.android.ebookreader.network.BuyBooksActivity;

public class BuyBasketBooksAction extends CatalogAction {
	public BuyBasketBooksAction(Activity activity) {
		super(activity, ActionCode.BASKET_BUY_ALL_BOOKS, "buyAllBooks");
	}

	@Override
	public boolean isVisible(NetworkTree tree) {
		return tree instanceof BasketCatalogTree && ((BasketCatalogTree)tree).canBeOpened();
	}

	@Override
	public boolean isEnabled(NetworkTree tree) {
		if (myLibrary.getStoredLoader(tree) != null) {
			return false;
		}
		final Set<String> bookIds = new HashSet<String>();
		for (FBTree t : tree.subtrees()) {
			if (t instanceof NetworkBookTree) {
				bookIds.add(((NetworkBookTree)t).Book.Id);
			}
		}
		final BasketItem item = (BasketItem)((BasketCatalogTree)tree).Item;
		return bookIds.equals(new HashSet(item.bookIds()));
	}

	@Override
	public void run(NetworkTree tree) {
		final ArrayList<NetworkBookTree> bookTrees = new ArrayList<NetworkBookTree>();
		for (FBTree t : tree.subtrees()) {
			if (t instanceof NetworkBookTree) {
				bookTrees.add((NetworkBookTree)t);
			}
		}
		BuyBooksActivity.run(myActivity, bookTrees);
	}
}
