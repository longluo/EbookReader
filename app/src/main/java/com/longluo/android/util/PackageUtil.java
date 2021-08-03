package com.longluo.android.util;

import java.util.Map;

import android.app.Activity;
import android.content.*;
import android.content.pm.*;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.CheckBox;

import com.longluo.zlibrary.core.options.ZLBooleanOption;
import com.longluo.zlibrary.core.resources.ZLResource;

public abstract class PackageUtil {
	private static Uri marketUri(String pkg) {
		return Uri.parse("market://details?id=" + pkg);
	}

	public static boolean canBeStarted(Context context, Intent intent, boolean checkSignature) {
		final PackageManager manager = context.getApplicationContext().getPackageManager();
		final ResolveInfo info =
			manager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
		if (info == null) {
			return false;
		}
		final ActivityInfo activityInfo = info.activityInfo;
		if (activityInfo == null) {
			return false;
		}
		if (!checkSignature) {
			return true;
		}
		return
			PackageManager.SIGNATURE_MATCH ==
			manager.checkSignatures(context.getPackageName(), activityInfo.packageName);
	}

	public static boolean installFromMarket(Activity activity, String pkg) {
		try {
			activity.startActivity(new Intent(
				Intent.ACTION_VIEW, marketUri(pkg)
			));
			return true;
		} catch (ActivityNotFoundException e) {
			return false;
		}
	}
}
