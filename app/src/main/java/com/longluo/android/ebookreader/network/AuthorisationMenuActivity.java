package com.longluo.android.ebookreader.network;

import android.app.Activity;
import android.content.*;
import android.net.Uri;

import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.network.INetworkLink;
import com.longluo.ebookreader.network.NetworkLibrary;
import com.longluo.ebookreader.network.urlInfo.UrlInfo;
import com.longluo.ebookreader.network.authentication.NetworkAuthenticationManager;

import com.longluo.android.ebookreader.api.PluginApi;
import com.longluo.android.ebookreader.network.litres.AutoRegistrationActivity;
import com.longluo.android.ebookreader.network.litres.UserRegistrationActivity;

public class AuthorisationMenuActivity extends MenuActivity {
	public static void runMenu(Context context, INetworkLink link) {
		context.startActivity(
			Util.intentByLink(new Intent(context, AuthorisationMenuActivity.class), link)
		);
	}

	public static void runMenu(Activity activity, INetworkLink link, int code) {
		activity.startActivityForResult(
			Util.intentByLink(new Intent(activity, AuthorisationMenuActivity.class), link), code
		);
	}

	private INetworkLink myLink;

	@Override
	protected void init() {
		final String baseUrl = getIntent().getData().toString();
		final ZLResource resource = NetworkLibrary.resource();

		setTitle(resource.getResource("authorisationMenuTitle").getValue());
		myLink = Util.networkLibrary(this).getLinkByUrl(baseUrl);

		if (myLink.getUrlInfo(UrlInfo.Type.SignIn) != null) {
			myInfos.add(new PluginApi.MenuActionInfo(
				Uri.parse(baseUrl + "/signIn"),
				resource.getResource("signIn").getValue(),
				0
			));
			if (myLink.authenticationManager() != null) {
				myInfos.add(new PluginApi.MenuActionInfo(
					Uri.parse(baseUrl + "/signUp"),
					resource.getResource("signUp").getValue(),
					1
				));
				myInfos.add(new PluginApi.MenuActionInfo(
					Uri.parse(baseUrl + "/quickBuy"),
					resource.getResource("quickBuy").getValue(),
					2
				));
			}
		}
	}

	@Override
	protected String getAction() {
		return Util.AUTHORISATION_ACTION;
	}

	@Override
	protected void runItem(final PluginApi.MenuActionInfo info) {
		try {
			final NetworkAuthenticationManager mgr = myLink.authenticationManager();
			if (info.getId().toString().endsWith("/signIn")) {
				Util.runAuthenticationDialog(AuthorisationMenuActivity.this, myLink, null);
			} else if (info.getId().toString().endsWith("/signUp")) {
				startActivity(Util.authorisationIntent(myLink, this, UserRegistrationActivity.class));
			} else if (info.getId().toString().endsWith("/quickBuy")) {
				startActivity(Util.authorisationIntent(myLink, this, AutoRegistrationActivity.class));
			}
		} catch (Exception e) {
			// do nothing
		}
	}
}
