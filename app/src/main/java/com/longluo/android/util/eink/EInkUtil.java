package com.longluo.android.util.eink;

import android.app.Activity;

import com.longluo.android.util.DeviceType;

public abstract class EInkUtil {
	public static void prepareSingleFullRefresh(Activity a) {
		final DeviceType deviceType = DeviceType.Instance();
		if (deviceType == DeviceType.NOOK || deviceType == DeviceType.NOOK12) {
			Nook2Util.setGL16Mode(a);
		}
	}
}
