package com.longluo.ebookreader.network.tree;

import com.longluo.zlibrary.core.image.ZLImage;
import com.longluo.zlibrary.core.network.ZLNetworkException;
import com.longluo.zlibrary.core.money.Money;

import com.longluo.ebookreader.network.TopUpItem;
import com.longluo.ebookreader.network.NetworkTree;
import com.longluo.ebookreader.network.authentication.NetworkAuthenticationManager;

public class TopUpTree extends NetworkTree {
	public final TopUpItem Item;

	TopUpTree(NetworkCatalogTree parentTree, TopUpItem item) {
		super(parentTree);
		Item = item;
	}

	@Override
	public String getName() {
		return Item.Title.toString();
	}

	@Override
	public String getSummary() {
		final NetworkAuthenticationManager mgr = getLink().authenticationManager();
		try {
			if (mgr != null && mgr.isAuthorised(false)) {
				final Money account = mgr.currentAccount();
				final CharSequence summary = Item.getSummary();
				if (account != null && summary != null) {
					return summary.toString().replace("%s", account.toString());
				}
			}
		} catch (ZLNetworkException e) {
		}
		return null;
	}

	@Override
	protected ZLImage createCover() {
		return createCoverForItem(Library, Item, true);
	}

	@Override
	protected String getStringId() {
		return "@TopUp Account";
	}
}
