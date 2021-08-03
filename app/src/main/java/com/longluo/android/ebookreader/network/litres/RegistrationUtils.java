package com.longluo.android.ebookreader.network.litres;

import java.util.*;
import java.lang.reflect.*;
import java.math.BigInteger;
import java.security.SecureRandom;

import android.content.Context;
import android.net.wifi.WifiManager;

public class RegistrationUtils {
	private final Context myContext;

	public RegistrationUtils(Context context) {
		myContext = context;
	}

	public String getAutoLogin(String email) {
		return email != null
			? "fbreader-auto-" + email.replace(".", "-").replace("@", "-at-") : null;
	}

	public String getAutoPassword() {
		try {
			final WifiManager wifi = (WifiManager)myContext.getSystemService(Context.WIFI_SERVICE);
			final String mac = wifi.getConnectionInfo().getMacAddress();
			if (mac.length() > 3) {
				return "XXX" + mac;
			}
		} catch (Exception e) {
		}
		return new BigInteger(50, new SecureRandom()).toString(32);
	}

	private List<String> myEMails;
	public List<String> eMails() {
		collectEMails();
		return myEMails;
	}

	public String firstEMail() {
		collectEMails();
		return myEMails.size() > 0 ? myEMails.get(0) : null;
	}

	private void collectEMails() {
		if (myEMails != null) {
			return;
		}
		try {
			final Class<?> cls$AccountManager = Class.forName("android.accounts.AccountManager");
			final Class<?> cls$Account = Class.forName("android.accounts.Account");

			final Method meth$AccountManager$get = cls$AccountManager.getMethod("get", Context.class);
			final Method meth$AccountManager$getAccountsByType = cls$AccountManager.getMethod("getAccountsByType", String.class);
			final Field fld$Account$name = cls$Account.getField("name");

			if (meth$AccountManager$get.getReturnType() == cls$AccountManager
					&& meth$AccountManager$getAccountsByType.getReturnType().getComponentType() == cls$Account
					&& fld$Account$name.getType() == String.class) {
				final Object mgr = meth$AccountManager$get.invoke(null, myContext);
				final Object[] accountsByType = (Object[]) meth$AccountManager$getAccountsByType.invoke(mgr, "com.google");
				myEMails = new ArrayList<String>(accountsByType.length);
				for (Object a: accountsByType) {
					final String value = (String) fld$Account$name.get(a);
					if (value != null && value.length() > 0) {
						myEMails.add(value);
					}
				}
				return;
			}
		} catch (ClassNotFoundException e) {
		} catch (NoSuchMethodException e) {
		} catch (NoSuchFieldException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		}
		myEMails = Collections.<String>emptyList();
	}
}
