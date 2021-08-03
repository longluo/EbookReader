package com.longluo.android.ebookreader.network.action;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.network.*;
import com.longluo.ebookreader.network.tree.NetworkCatalogTree;
import com.longluo.ebookreader.network.urlInfo.UrlInfo;

import com.longluo.android.ebookreader.network.Util;

public class OpenInBrowserAction extends CatalogAction {
	public OpenInBrowserAction(Activity activity) {
		super(activity, ActionCode.OPEN_IN_BROWSER, "openInBrowser");
	}

	@Override
	public boolean isVisible(NetworkTree tree) {
		if (!super.isVisible(tree)) {
			return false;
		}

		final NetworkCatalogItem item = ((NetworkCatalogTree)tree).Item;
		if (!(item instanceof NetworkURLCatalogItem)) {
			return false;
		}

		return ((NetworkURLCatalogItem)item).getUrl(UrlInfo.Type.HtmlPage) != null;
	}

	@Override
	public void run(NetworkTree tree) {
		final String url =
			((NetworkURLCatalogItem)((NetworkCatalogTree)tree).Item).getUrl(UrlInfo.Type.HtmlPage);

		if (Util.isOurLink(url)) {
			Util.openInBrowser(myActivity, url);
		} else {
			final ZLResource buttonResource = ZLResource.resource("dialog").getResource("button");
			final String message = NetworkLibrary.resource().getResource("confirmQuestions").getResource("openInBrowser").getValue();
			new AlertDialog.Builder(myActivity)
				.setTitle(tree.getName())
				.setMessage(message)
				.setIcon(0)
				.setPositiveButton(buttonResource.getResource("yes").getValue(), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Util.openInBrowser(myActivity, url);
					}
				})
				.setNegativeButton(buttonResource.getResource("no").getValue(), null)
				.create().show();
		}
	}
}
