package com.longluo.android.ebookreader.network;

import android.content.*;
import android.os.IBinder;

public class BookDownloaderServiceConnection implements ServiceConnection {
	private volatile Runnable myAction;
	private volatile BookDownloaderInterface myInterface;

	synchronized void bindToService(Context context, Runnable action) {
		if (myInterface != null) {
			if (action != null) {
				action.run();
			}
		} else {
			myAction = action;
			context.bindService(
				new Intent(context, BookDownloaderService.class),
				this,
				Context.BIND_AUTO_CREATE
			);
		}
	}

	synchronized void unbind(Context context) {
		myAction = null;
		if (myInterface != null) {
			context.unbindService(this);
			myInterface = null;
		}
	}

	public synchronized void onServiceConnected(ComponentName className, IBinder service) {
		myInterface = BookDownloaderInterface.Stub.asInterface(service);
		if (myAction != null) {
			myAction.run();
			myAction = null;
		}
	}

	public synchronized void onServiceDisconnected(ComponentName name) {
		myInterface = null;
	}

	public synchronized boolean isBeingDownloaded(String url) {
		if (myInterface != null) {
			try {
				return myInterface.isBeingDownloaded(url);
			} catch (android.os.RemoteException e) {
			}
		}
		return false;
	}
}
