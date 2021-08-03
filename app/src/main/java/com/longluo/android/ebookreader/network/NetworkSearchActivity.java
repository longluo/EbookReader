package com.longluo.android.ebookreader.network;

import android.app.Activity;
import android.app.SearchManager;
import android.os.Bundle;
import android.content.Intent;

import com.longluo.zlibrary.core.util.MimeType;

import com.longluo.ebookreader.network.NetworkLibrary;
import com.longluo.ebookreader.network.NetworkTree;
import com.longluo.ebookreader.network.tree.SearchCatalogTree;

import com.longluo.android.ebookreader.network.auth.ActivityNetworkContext;

public class NetworkSearchActivity extends Activity {
	private final ActivityNetworkContext myNetworkContext = new ActivityNetworkContext(this);

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Thread.setDefaultUncaughtExceptionHandler(new com.longluo.zlibrary.ui.android.library.UncaughtExceptionHandler(this));

		final Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			final Bundle data = intent.getBundleExtra(SearchManager.APP_DATA);
			if (data != null) {
				final NetworkLibrary library = Util.networkLibrary(this);
				final NetworkTree.Key key =
					(NetworkTree.Key)data.getSerializable(NetworkLibraryActivity.TREE_KEY_KEY);
				final NetworkTree tree = library.getTreeByKey(key);
				if (tree instanceof SearchCatalogTree) {
					final SearchCatalogTree searchTree = (SearchCatalogTree)tree;
					final String pattern = intent.getStringExtra(SearchManager.QUERY);
					final MimeType mime = searchTree.getMimeType();
					if (MimeType.APP_ATOM_XML.weakEquals(mime)) {
						searchTree.startItemsLoader(myNetworkContext, pattern);
					} else if (MimeType.TEXT_HTML.weakEquals(mime)) {
						Util.openInBrowser(this, searchTree.getUrl(pattern));
					}
				}
			}
		}
		finish();
	}
}
