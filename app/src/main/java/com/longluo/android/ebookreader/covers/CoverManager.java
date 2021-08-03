package com.longluo.android.ebookreader.covers;

import java.util.concurrent.*;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.longluo.zlibrary.core.image.ZLImage;
import com.longluo.zlibrary.core.image.ZLImageProxy;

import com.longluo.zlibrary.ui.android.image.ZLAndroidImageData;
import com.longluo.zlibrary.ui.android.image.ZLAndroidImageManager;

import com.longluo.ebookreader.tree.FBTree;

public class CoverManager {
	final CoverCache Cache = new CoverCache();

	private static class MinPriorityThreadFactory implements ThreadFactory {
		private final ThreadFactory myDefaultThreadFactory = Executors.defaultThreadFactory();

		public Thread newThread(Runnable r) {
			final Thread th = myDefaultThreadFactory.newThread(r);
			th.setPriority(Thread.MIN_PRIORITY);
			return th;
		}
	}
	private final ExecutorService myPool = Executors.newFixedThreadPool(1, new MinPriorityThreadFactory());

	private final Activity myActivity;
	private final ZLImageProxy.Synchronizer myImageSynchronizer;
	private final int myCoverWidth;
	private final int myCoverHeight;

	public CoverManager(Activity activity, ZLImageProxy.Synchronizer synchronizer, int coverWidth, int coverHeight) {
		myActivity = activity;
		myImageSynchronizer = synchronizer;
		myCoverWidth = coverWidth;
		myCoverHeight = coverHeight;
	}

	void runOnUiThread(Runnable runnable) {
		myActivity.runOnUiThread(runnable);
	}

	void setupCoverView(ImageView coverView) {
		coverView.getLayoutParams().width = myCoverWidth;
		coverView.getLayoutParams().height = myCoverHeight;
		coverView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		coverView.requestLayout();
	}

	Bitmap getBitmap(ZLImage image) {
		final ZLAndroidImageManager mgr = (ZLAndroidImageManager)ZLAndroidImageManager.Instance();
		final ZLAndroidImageData data = mgr.getImageData(image);
		if (data == null) {
			return null;
		}
		return data.getBitmap(2 * myCoverWidth, 2 * myCoverHeight);
	}

	void setCoverForView(CoverHolder holder, ZLImageProxy image) {
		synchronized (holder) {
			try {
				final Bitmap coverBitmap = Cache.getBitmap(holder.Key);
				if (coverBitmap != null) {
					holder.CoverView.setImageBitmap(coverBitmap);
				} else if (holder.coverBitmapTask == null) {
					holder.coverBitmapTask = myPool.submit(holder.new CoverBitmapRunnable(image));
				}
			} catch (CoverCache.NullObjectException e) {
			}
		}
	}

	private CoverHolder getHolder(ImageView coverView, FBTree tree) {
		CoverHolder holder = (CoverHolder)coverView.getTag();
		if (holder == null) {
			holder = new CoverHolder(this, coverView, tree.getUniqueKey());
			coverView.setTag(holder);
		} else {
			holder.setKey(tree.getUniqueKey());
		}
		return holder;
	}

	public boolean trySetCoverImage(ImageView coverView, FBTree tree) {
		final CoverHolder holder = getHolder(coverView, tree);

		Bitmap coverBitmap;
		try {
			coverBitmap = Cache.getBitmap(holder.Key);
		} catch (CoverCache.NullObjectException e) {
			return false;
		}

		if (coverBitmap == null) {
			final ZLImage cover = tree.getCover();
			if (cover instanceof ZLImageProxy) {
				final ZLImageProxy img = (ZLImageProxy)cover;
				if (img.isSynchronized()) {
					setCoverForView(holder, img);
				} else {
					img.startSynchronization(
						myImageSynchronizer,
						holder.new CoverSyncRunnable(img)
					);
				}
			} else if (cover != null) {
				coverBitmap = getBitmap(cover);
			}
		}
		if (coverBitmap != null) {
			holder.CoverView.setImageBitmap(coverBitmap);
			return true;
		}
		return false;
	}
}