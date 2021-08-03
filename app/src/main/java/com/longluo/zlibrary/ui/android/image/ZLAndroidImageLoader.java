package com.longluo.zlibrary.ui.android.image;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import android.os.Handler;
import android.os.Message;

import com.longluo.zlibrary.core.image.ZLImageProxy;

class ZLAndroidImageLoader {
	void startImageLoading(final ZLImageProxy.Synchronizer synchronizer, final ZLImageProxy image, Runnable postAction) {
		synchronized (myOnImageSyncRunnables) {
			LinkedList<Runnable> runnables = myOnImageSyncRunnables.get(image.getId());
			if (runnables != null) {
				if (postAction != null && !runnables.contains(postAction)) {
					runnables.add(postAction);
				}
				return;
			}

			runnables = new LinkedList<Runnable>();
			if (postAction != null) {
				runnables.add(postAction);
			}
			myOnImageSyncRunnables.put(image.getId(), runnables);
		}

		final ExecutorService pool =
			image.sourceType() == ZLImageProxy.SourceType.FILE
				? mySinglePool : myPool;
		pool.execute(new Runnable() {
			public void run() {
				synchronizer.synchronize(image, new Runnable() {
					public void run() {
						myImageSynchronizedHandler.fireMessage(image.getId());
					}
				});
			}
		});
	}

	private static class MinPriorityThreadFactory implements ThreadFactory {
		private final ThreadFactory myDefaultThreadFactory = Executors.defaultThreadFactory();

		public Thread newThread(Runnable r) {
			final Thread th = myDefaultThreadFactory.newThread(r);
			th.setPriority(Thread.MIN_PRIORITY);
			return th;
		}
	}

	private static final int IMAGE_LOADING_THREADS_NUMBER = 3; // TODO: how many threads ???

	private final ExecutorService myPool = Executors.newFixedThreadPool(IMAGE_LOADING_THREADS_NUMBER, new MinPriorityThreadFactory());
	private final ExecutorService mySinglePool = Executors.newFixedThreadPool(1, new MinPriorityThreadFactory());

	private final HashMap<String,LinkedList<Runnable>> myOnImageSyncRunnables = new HashMap<String,LinkedList<Runnable>>();

	private class ImageSynchronizedHandler extends Handler {
		@Override
		public void handleMessage(Message message) {
			final String imageUrl = (String)message.obj;
			final LinkedList<Runnable> runables;
			synchronized (myOnImageSyncRunnables) {
				runables = myOnImageSyncRunnables.remove(imageUrl);
			}
			for (Runnable runnable : runables) {
				runnable.run();
			}
		}

		public void fireMessage(String imageUrl) {
			sendMessage(obtainMessage(0, imageUrl));
		}
	};

	private final ImageSynchronizedHandler myImageSynchronizedHandler = new ImageSynchronizedHandler();
}
