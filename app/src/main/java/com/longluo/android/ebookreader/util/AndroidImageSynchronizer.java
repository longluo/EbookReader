package com.longluo.android.ebookreader.util;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import android.app.Activity;
import android.app.Service;
import android.content.*;
import android.os.IBinder;

import com.longluo.zlibrary.core.image.ZLImageProxy;
import com.longluo.zlibrary.core.image.ZLImageSimpleProxy;
import com.longluo.zlibrary.ui.android.image.ZLAndroidImageManager;
import com.longluo.zlibrary.ui.android.image.ZLBitmapImage;

import com.longluo.ebookreader.formats.ExternalFormatPlugin;
import com.longluo.ebookreader.formats.PluginImage;
import com.longluo.android.ebookreader.api.FBReaderIntents;
import com.longluo.android.ebookreader.formatPlugin.CoverReader;

public class AndroidImageSynchronizer implements ZLImageProxy.Synchronizer {
	private static final class Connection implements ServiceConnection {
		private final ExecutorService myExecutor = Executors.newSingleThreadExecutor();

		private final ExternalFormatPlugin myPlugin;
		private volatile CoverReader Reader;
		private final List<Runnable> myPostActions = new LinkedList<Runnable>();

		Connection(ExternalFormatPlugin plugin) {
			myPlugin = plugin;
		}

		synchronized void runOrAddAction(Runnable action) {
			if (Reader != null) {
				myExecutor.execute(action);
			} else {
				myPostActions.add(action);
			}
		}

		public synchronized void onServiceConnected(ComponentName className, IBinder binder) {
			Reader = CoverReader.Stub.asInterface(binder);
			for (Runnable action : myPostActions) {
				myExecutor.execute(action);
			}
			myPostActions.clear();
		}

		public synchronized void onServiceDisconnected(ComponentName className) {
			Reader = null;
		}
	}

	private final Context myContext;
	private final Map<ExternalFormatPlugin,Connection> myConnections =
		new HashMap<ExternalFormatPlugin,Connection>();

	public AndroidImageSynchronizer(Activity activity) {
		myContext = activity;
	}

	public AndroidImageSynchronizer(Service service) {
		myContext = service;
	}

	@Override
	public void startImageLoading(ZLImageProxy image, Runnable postAction) {
		final ZLAndroidImageManager manager = (ZLAndroidImageManager)ZLAndroidImageManager.Instance();
		manager.startImageLoading(this, image, postAction);
	}

	@Override
	public void synchronize(ZLImageProxy image, final Runnable postAction) {
		if (image.isSynchronized()) {
			// TODO: also check if image is under synchronization
			if (postAction != null) {
				postAction.run();
			}
		} else if (image instanceof ZLImageSimpleProxy) {
			((ZLImageSimpleProxy)image).synchronize();
			if (postAction != null) {
				postAction.run();
			}
		} else if (image instanceof PluginImage) {
			final PluginImage pluginImage = (PluginImage)image;
			final Connection connection = getConnection(pluginImage.Plugin);
			connection.runOrAddAction(new Runnable() {
				public void run() {
					try {
						pluginImage.setRealImage(new ZLBitmapImage(connection.Reader.readBitmap(pluginImage.File.getPath(), Integer.MAX_VALUE, Integer.MAX_VALUE)));
					} catch (Throwable t) {
						t.printStackTrace();
					}
					if (postAction != null) {
						postAction.run();
					}
				}
			});
		} else {
			throw new RuntimeException("Cannot synchronize " + image.getClass());
		}
	}

	public synchronized void clear() {
		for (ServiceConnection connection : myConnections.values()) {
			myContext.unbindService(connection);
		}
		myConnections.clear();
	}

	private synchronized Connection getConnection(ExternalFormatPlugin plugin) {
		Connection connection = myConnections.get(plugin);
		if (connection == null) {
			connection = new Connection(plugin);
			myConnections.put(plugin, connection);
			myContext.bindService(
				new Intent(FBReaderIntents.Action.PLUGIN_CONNECT_COVER_SERVICE)
					.setPackage(plugin.packageName()),
				connection,
				Context.BIND_AUTO_CREATE
			);
		}
		return connection;
	}
}
