package com.longluo.ebookreader.network.tree;

import com.longluo.zlibrary.core.network.ZLNetworkContext;
import com.longluo.zlibrary.core.network.ZLNetworkException;

import com.longluo.ebookreader.network.*;

class Searcher extends NetworkItemsLoader {
	private final String myPattern;
	private volatile boolean myItemFound;

	Searcher(ZLNetworkContext nc, SearchCatalogTree tree, String pattern) {
		super(nc, tree);
		myPattern = pattern;
	}

	@Override
	public void doBefore() {
		Tree.Library.NetworkSearchPatternOption.setValue(myPattern);
	}

	@Override
	public void interrupt() {
		// Searcher is not interruptable at this moment
	}

	@Override
	public void load() throws ZLNetworkException {
		final SearchItem item = (SearchItem)Tree.Item;
		if (myPattern.equals(item.getPattern())) {
			if (Tree.hasChildren()) {
				myItemFound = true;
				Tree.Library.fireModelChangedEvent(
					NetworkLibrary.ChangeListener.Code.Found, Tree
				);
			} else {
				Tree.Library.fireModelChangedEvent(
					NetworkLibrary.ChangeListener.Code.NotFound
				);
			}
		} else {
			item.runSearch(NetworkContext, this, myPattern);
		}
	}

	@Override
	public synchronized void onNewItem(final NetworkItem item) {
		if (!myItemFound) {
			((SearchCatalogTree)Tree).setPattern(myPattern);
			Tree.clearCatalog();
			Tree.Library.fireModelChangedEvent(
				NetworkLibrary.ChangeListener.Code.Found, Tree
			);
			myItemFound = true;
		}
		super.onNewItem(item);
	}

	@Override
	protected void onFinish(ZLNetworkException exception, boolean interrupted) {
		if (!interrupted && !myItemFound) {
			Tree.Library.fireModelChangedEvent(NetworkLibrary.ChangeListener.Code.NotFound);
		}
	}
}
