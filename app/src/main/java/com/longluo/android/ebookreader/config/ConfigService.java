package com.longluo.android.ebookreader.config;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ConfigService extends Service {
	private ConfigInterface.Stub myConfig;

	@Override
	public IBinder onBind(Intent intent) {
		return myConfig;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		myConfig = new SQLiteConfig(this);
	}

	@Override
	public void onDestroy() {
		if (myConfig != null) {
			// TODO: close db
			myConfig = null;
		}
		super.onDestroy();
	}
}
