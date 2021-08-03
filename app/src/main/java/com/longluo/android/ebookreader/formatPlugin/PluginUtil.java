package com.longluo.android.ebookreader.formatPlugin;

import android.content.Intent;

import com.longluo.ebookreader.formats.ExternalFormatPlugin;

public abstract class PluginUtil {
	public static Intent createIntent(ExternalFormatPlugin plugin, String action) {
		return new Intent(action).setPackage(plugin.packageName());
	}
}
