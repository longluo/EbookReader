package com.longluo.android.ebookreader.network;

import android.os.Bundle;

public class NetworkLibraryPrimaryActivity extends NetworkLibraryActivity {
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		myNetworkContext.reloadCookie();
	}
}
