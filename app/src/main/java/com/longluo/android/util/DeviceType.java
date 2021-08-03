package com.longluo.android.util;

import android.os.Build;

public enum DeviceType {
	GENERIC,
	YOTA_PHONE,
	KINDLE_FIRE_1ST_GENERATION,
	KINDLE_FIRE_2ND_GENERATION,
	KINDLE_FIRE_HD,
	NOOK,
	NOOK12,
	EKEN_M001,
	PAN_DIGITAL,
	SAMSUNG_GT_S5830;

	private static DeviceType ourInstance;
	public static DeviceType Instance() {
		if (ourInstance == null) {
			if ("YotaPhone".equals(Build.BRAND)) {
				ourInstance = YOTA_PHONE;
			} else if ("GT-S5830".equals(Build.MODEL)) {
				ourInstance = SAMSUNG_GT_S5830;
			} else if ("Amazon".equals(Build.MANUFACTURER)) {
				if ("Kindle Fire".equals(Build.MODEL)) {
					ourInstance = KINDLE_FIRE_1ST_GENERATION;
				} else if ("KFOT".equals(Build.MODEL)) {
					ourInstance = KINDLE_FIRE_2ND_GENERATION;
				} else {
					ourInstance = KINDLE_FIRE_HD;
				}
			} else if (Build.DISPLAY != null && Build.DISPLAY.contains("simenxie")) {
				ourInstance = EKEN_M001;
			} else if ("PD_Novel".equals(Build.MODEL)) {
				ourInstance = PAN_DIGITAL;
			} else if ("BarnesAndNoble".equals(Build.MANUFACTURER) &&
					   "zoom2".equals(Build.DEVICE) &&
					   Build.MODEL != null &&
					   ("NOOK".equals(Build.MODEL) ||
						"unknown".equals(Build.MODEL) ||
						Build.MODEL.startsWith("BNRV"))) {
				if (Build.VERSION.INCREMENTAL != null &&
					(Build.VERSION.INCREMENTAL.startsWith("1.2") ||
					 Build.VERSION.INCREMENTAL.startsWith("1.3"))) {
					ourInstance = NOOK12;
				} else {
					ourInstance = NOOK;
				}
			} else {
				ourInstance = GENERIC;
			}
		}
		return ourInstance;
	}

	public boolean hasNoHardwareMenuButton() {
		return this == EKEN_M001 || this == PAN_DIGITAL;
	}

	public boolean hasButtonLightsBug() {
		return this == SAMSUNG_GT_S5830;
	}

	public boolean isEInk() {
		return this == NOOK || this == NOOK12;
	}

	public boolean hasStandardSearchDialog() {
		return this != NOOK && this != NOOK12;
	}
}
