package com.longluo.android.ebookreader.network.action;

import android.app.Activity;
import android.content.Intent;

import com.longluo.zlibrary.core.network.ZLNetworkContext;
import com.longluo.zlibrary.core.network.ZLNetworkException;

import com.longluo.ebookreader.network.NetworkTree;

import com.longluo.android.ebookreader.network.NetworkBookInfoActivity;
import com.longluo.android.ebookreader.network.NetworkLibraryActivity;

import com.longluo.android.util.OrientationUtil;
import com.longluo.android.util.UIUtil;

public class ShowBookInfoAction extends BookAction {
	private final ZLNetworkContext myNetworkContext;

	public ShowBookInfoAction(Activity activity, ZLNetworkContext nc) {
		super(activity, ActionCode.SHOW_BOOK_ACTIVITY, "bookInfo");
		myNetworkContext = nc;
	}

	@Override
	public void run(final NetworkTree tree) {
		if (getBook(tree).isFullyLoaded()) {
			showBookInfo(tree);
		} else {
			UIUtil.wait("loadInfo", new Runnable() {
				public void run() {
					getBook(tree).loadFullInformation(myNetworkContext);
					myActivity.runOnUiThread(new Runnable() {
						public void run() {
							showBookInfo(tree);
						}
					});
				}
			}, myActivity);
		}
	}

	private void showBookInfo(NetworkTree tree) {
		OrientationUtil.startActivityForResult(
			myActivity,
			new Intent(myActivity, NetworkBookInfoActivity.class)
				.putExtra(NetworkLibraryActivity.TREE_KEY_KEY, tree.getUniqueKey()),
			1
		);
	}
}
