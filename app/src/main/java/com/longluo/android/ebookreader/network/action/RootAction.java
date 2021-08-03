package com.longluo.android.ebookreader.network.action;

import android.app.Activity;

import com.longluo.ebookreader.network.NetworkTree;
import com.longluo.ebookreader.network.tree.RootTree;

public abstract class RootAction extends Action {
	protected RootAction(Activity activity, int code, String resourceKey, int iconId) {
		super(activity, code, resourceKey, iconId);
	}

	@Override
	public boolean isVisible(NetworkTree tree) {
		return tree instanceof RootTree;
	}
}
