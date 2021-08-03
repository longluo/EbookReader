package com.longluo.android.ebookreader.sync;

import android.app.Service;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.longluo.util.ComparisonUtil;

import com.longluo.zlibrary.core.network.ZLNetworkException;
import com.longluo.zlibrary.core.network.ZLNetworkRequest;
import com.longluo.zlibrary.core.options.ZLEnumOption;

import com.longluo.ebookreader.fbreader.options.SyncOptions;
import com.longluo.ebookreader.network.sync.SyncUtil;

import com.longluo.android.ebookreader.network.auth.ServiceNetworkContext;

class SyncNetworkContext extends ServiceNetworkContext {
	private final SyncOptions mySyncOptions;
	private final ZLEnumOption<SyncOptions.Condition> myFeatureOption;

	private volatile String myAccountName;

	SyncNetworkContext(Service service, SyncOptions syncOptions, ZLEnumOption<SyncOptions.Condition> featureOption) {
		super(service);
		mySyncOptions = syncOptions;
		myFeatureOption = featureOption;
	}

	@Override
	protected void perform(ZLNetworkRequest request, int socketTimeout, int connectionTimeout) throws ZLNetworkException {
		if (!canPerformRequest()) {
			throw new SynchronizationDisabledException();
		}
		final String accountName = SyncUtil.getAccountName(this);
		if (!ComparisonUtil.equal(myAccountName, accountName)) {
			reloadCookie();
			myAccountName = accountName;
		}
		super.perform(request, socketTimeout, connectionTimeout);
	}

	private boolean canPerformRequest() {
		if (!mySyncOptions.Enabled.getValue()) {
			return false;
		}

		switch (myFeatureOption.getValue()) {
			default:
			case never:
				return false;
			case always:
			{
				final NetworkInfo info = getActiveNetworkInfo();
				return info != null && info.isConnected();
			}
			case viaWifi:
			{
				final NetworkInfo info = getActiveNetworkInfo();
				return
					info != null &&
					info.isConnected() &&
					info.getType() == ConnectivityManager.TYPE_WIFI;
			}
		}
	}
}
