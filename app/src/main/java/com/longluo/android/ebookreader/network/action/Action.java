package com.longluo.android.ebookreader.network.action;

import android.app.Activity;

import com.longluo.ebookreader.network.NetworkTree;
import com.longluo.ebookreader.network.NetworkLibrary;

import com.longluo.android.ebookreader.network.Util;

public abstract class Action {
	public final int Code;
	public final int IconId;

	protected final Activity myActivity;
	protected final NetworkLibrary myLibrary;
	private final String myResourceKey;

	protected Action(Activity activity, int code, String resourceKey, int iconId) {
		myActivity = activity;
		myLibrary = Util.networkLibrary(activity);
		Code = code;
		myResourceKey = resourceKey;
		IconId = iconId;
	}

	public abstract boolean isVisible(NetworkTree tree);

	public boolean isEnabled(NetworkTree tree) {
		return true;
	}

	public abstract void run(NetworkTree tree);

	public String getContextLabel(NetworkTree tree) {
		return
			NetworkLibrary.resource().getResource(myResourceKey).getValue();
	}

	public String getOptionsLabel(NetworkTree tree) {
		return
			NetworkLibrary.resource().getResource("menu").getResource(myResourceKey).getValue();
	}
}
