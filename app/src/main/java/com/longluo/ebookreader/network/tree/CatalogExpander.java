package com.longluo.ebookreader.network.tree;

import com.longluo.zlibrary.core.network.ZLNetworkContext;
import com.longluo.zlibrary.core.network.ZLNetworkException;

import com.longluo.ebookreader.network.*;
import com.longluo.ebookreader.network.authentication.NetworkAuthenticationManager;

class CatalogExpander extends NetworkItemsLoader {
	private final boolean myAuthenticate;
	private final boolean myResumeNotLoad;

	CatalogExpander(ZLNetworkContext nc, NetworkCatalogTree tree, boolean authenticate, boolean resumeNotLoad) {
		super(nc, tree);
		myAuthenticate = authenticate;
		myResumeNotLoad = resumeNotLoad;
	}

	@Override
	public void doBefore() throws ZLNetworkException {
		final INetworkLink link = Tree.getLink();
		if (myAuthenticate && link != null && link.authenticationManager() != null) {
			final NetworkAuthenticationManager mgr = link.authenticationManager();
			try {
				if (mgr.isAuthorised(true) && mgr.needsInitialization()) {
					new Thread() {
						public void run() {
							try {
								mgr.initialize();
							} catch (ZLNetworkException e) {
								e.printStackTrace();
							}
						}
					}.start();
				}
			} catch (ZLNetworkException e) {
				mgr.logOut();
			}
		}
	}

	@Override
	public void load() throws ZLNetworkException {
		if (myResumeNotLoad) {
			Tree.Item.resumeLoading(this);
		} else {
			Tree.Item.loadChildren(this);
		}
	}

	@Override
	protected void onFinish(ZLNetworkException exception, boolean interrupted) {
		if (interrupted && (!Tree.Item.supportsResumeLoading() || exception != null)) {
			Tree.clearCatalog();
		} else {
			Tree.removeUnconfirmedItems();
			if (!interrupted) {
				if (exception != null) {
					Tree.Library.fireModelChangedEvent(
						NetworkLibrary.ChangeListener.Code.NetworkError, exception.getMessage()
					);
				} else {
					Tree.updateLoadedTime();
					if (Tree.subtrees().isEmpty()) {
						Tree.Library.fireModelChangedEvent(
							NetworkLibrary.ChangeListener.Code.EmptyCatalog
						);
					}
				}
			}
			Tree.Library.invalidateVisibility();
			Tree.Library.synchronize();
		}
	}
}
