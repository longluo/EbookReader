package com.longluo.android.ebookreader.httpd;

import java.io.IOException;

import android.app.Service;
import android.content.*;
import android.os.IBinder;
import android.os.RemoteException;

import com.longluo.android.ebookreader.util.AndroidImageSynchronizer;

public class DataService extends Service {
	final AndroidImageSynchronizer ImageSynchronizer = new AndroidImageSynchronizer(this);

	public static class Connection implements ServiceConnection {
		private DataInterface myDataInterface;

		public void onServiceConnected(ComponentName componentName, IBinder binder) {
			myDataInterface = DataInterface.Stub.asInterface(binder);
		}

		public void onServiceDisconnected(ComponentName componentName) {
			myDataInterface = null;
		}

		public int getPort() {
			try {
				return myDataInterface != null ? myDataInterface.getPort() : -1;
			} catch (RemoteException e) {
				return -1;
			}
		}
	}

	private DataServer myServer;
	private volatile int myPort = -1;

	@Override
	public void onCreate() {
		new Thread(new Runnable() {
			public void run () {
				for (int port = 12000; port < 12500; ++port) {
					try {
						myServer = new DataServer(DataService.this, port);
						myServer.start();
						myPort = port;
						break;
					} catch (IOException e) {
						myServer = null;
					}
				}
			}
		}).start();
	}

	@Override
	public void onDestroy() {
		if (myServer != null) {
			new Thread(new Runnable() {
				public void run () {
					if (myServer != null) {
						myServer.stop();
						myServer = null;
					}
				}
			}).start();
		}
		ImageSynchronizer.clear();
		super.onDestroy();
	}

	public IBinder onBind(Intent intent) {
		return new DataInterface.Stub() {
			public int getPort() {
				return myPort;
			}
		};
	}
}
