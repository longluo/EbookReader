package com.longluo.android.ebookreader.network;

import java.util.*;

import android.content.*;
import android.net.Uri;

import com.longluo.zlibrary.core.money.Money;

import com.longluo.ebookreader.network.INetworkLink;
import com.longluo.ebookreader.network.NetworkLibrary;
import com.longluo.ebookreader.network.urlInfo.UrlInfo;
import com.longluo.ebookreader.network.authentication.NetworkAuthenticationManager;

import com.longluo.android.util.PackageUtil;

import com.longluo.android.ebookreader.api.PluginApi;

public class TopupMenuActivity extends MenuActivity {
	private static final String AMOUNT_KEY = "topup:amount";
	private static final String CURRENCY_KEY = "topup:currency";

	public static boolean isTopupSupported(INetworkLink link) {
		// TODO: more correct check
		return link.getUrlInfo(UrlInfo.Type.TopUp) != null;
	}

	public static void runMenu(Context context, INetworkLink link, Money amount) {
		final Intent intent =
			Util.intentByLink(new Intent(context, TopupMenuActivity.class), link);
		intent.putExtra(AMOUNT_KEY, amount);
		context.startActivity(intent);
	}

	private INetworkLink myLink;
	private Money myAmount;

	@Override
	protected void init() {
		setTitle(NetworkLibrary.resource().getResource("topupTitle").getValue());
		final String url = getIntent().getData().toString();
		myLink = Util.networkLibrary(this).getLinkByUrl(url);
		myAmount = (Money)getIntent().getSerializableExtra(AMOUNT_KEY);

		if (myLink.getUrlInfo(UrlInfo.Type.TopUp) != null) {
			myInfos.add(new PluginApi.MenuActionInfo(
				Uri.parse(url + "/browser"),
				NetworkLibrary.resource().getResource("topupViaBrowser").getValue(),
				100
			));
		}
	}

	@Override
	protected String getAction() {
		return Util.TOPUP_ACTION;
	}

	@Override
	protected void runItem(final PluginApi.MenuActionInfo info) {
		try {
			doTopup(new Runnable() {
				public void run() {
					try {
						final NetworkAuthenticationManager mgr = myLink.authenticationManager();
						if (info.getId().toString().endsWith("/browser")) {
							// TODO: put amount
							if (mgr != null) {
								Util.openInBrowser(TopupMenuActivity.this, mgr.topupLink(myAmount));
							}
						} else {
							final Intent intent = new Intent(getAction(), info.getId());
							if (mgr != null) {
								for (Map.Entry<String,String> entry : mgr.getTopupData().entrySet()) {
									intent.putExtra(entry.getKey(), entry.getValue());
								}
							}
							if (myAmount != null) {
								intent.putExtra(AMOUNT_KEY, myAmount.Amount);
							}
							if (PackageUtil.canBeStarted(TopupMenuActivity.this, intent, true)) {
								startActivity(intent);
							}
						}
					} catch (ActivityNotFoundException e) {
					}
				}
			});
		} catch (Exception e) {
			// do nothing
		}
	}

	private void doTopup(final Runnable action) {
		final NetworkAuthenticationManager mgr = myLink.authenticationManager();
		if (mgr.mayBeAuthorised(false)) {
			action.run();
		} else {
			Util.runAuthenticationDialog(this, myLink, action);
		}
	}
}
