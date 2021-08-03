package com.longluo.android.ebookreader.api;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ApiService extends Service {
	@Override
	public IBinder onBind(Intent intent) {
		return new ApiServerImplementation(this);
	}
}
