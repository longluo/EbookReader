package com.longluo.android.ebookreader.network.action;

import android.app.Activity;

import com.longluo.ebookreader.network.NetworkBookItem;
import com.longluo.ebookreader.network.NetworkTree;
import com.longluo.ebookreader.network.tree.NetworkBookTree;

abstract class BookAction extends Action {
	protected BookAction(Activity activity, int code, String resourceKey) {
		super(activity, code, resourceKey, -1);
	}

	@Override
	public boolean isVisible(NetworkTree tree) {
		return tree instanceof NetworkBookTree;
	}

	protected NetworkBookItem getBook(NetworkTree tree) {
		return ((NetworkBookTree)tree).Book;
	}
}
