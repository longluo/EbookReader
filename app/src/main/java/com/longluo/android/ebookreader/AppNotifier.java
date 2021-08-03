package com.longluo.android.ebookreader;

import java.util.ArrayList;

import android.app.*;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.longluo.ebookreader.R;
import com.longluo.zlibrary.ui.android.network.SQLiteCookieDatabase;

import com.longluo.ebookreader.Paths;
import com.longluo.ebookreader.fbreader.FBReaderApp;
import com.longluo.ebookreader.network.NetworkImage;
import com.longluo.ebookreader.network.sync.SyncData;
import com.longluo.ebookreader.network.urlInfo.UrlInfo;

import com.longluo.android.ebookreader.network.BookDownloaderService;
import com.longluo.android.ebookreader.sync.MissingBookActivity;

class AppNotifier implements FBReaderApp.Notifier {
	private final Activity myActivity;
	private final ArrayList<String> myLatestHashes = new ArrayList<String>();
	private volatile long myLatestNotificationStamp;

	AppNotifier(Activity activity) {
		myActivity = activity;
	}

	@Override
	public void showMissingBookNotification(final SyncData.ServerBookInfo info) {
		synchronized (this) {
			myLatestHashes.retainAll(info.Hashes);
			if (!myLatestHashes.isEmpty() &&
				myLatestNotificationStamp > System.currentTimeMillis() - 5 * 60 * 1000) {
				return;
			}
			myLatestHashes.addAll(info.Hashes);
			myLatestNotificationStamp = System.currentTimeMillis();
		}
		new Thread() {
			public void run() {
				showMissingBookNotificationInternal(info);
			}
		}.start();
	}

	private void showMissingBookNotificationInternal(SyncData.ServerBookInfo info) {
		final String errorTitle = MissingBookActivity.errorTitle();

		final NotificationManager notificationManager =
			(NotificationManager)myActivity.getSystemService(Activity.NOTIFICATION_SERVICE);
		final NotificationCompat.Builder builder = new NotificationCompat.Builder(myActivity)
			.setSmallIcon(R.drawable.fbreader)
			.setTicker(errorTitle)
			.setContentTitle(errorTitle)
			.setContentText(info.Title);

		if (info.ThumbnailUrl != null) {
			SQLiteCookieDatabase.init(myActivity);
			final NetworkImage thumbnail = new NetworkImage(info.ThumbnailUrl, Paths.systemInfo(myActivity));
			thumbnail.synchronize();
			try {
				builder.setLargeIcon(
					BitmapFactory.decodeStream(thumbnail.getRealImage().inputStream())
				);
			} catch (Throwable t) {
				// ignore
			}
		}

		final int notificationId = info.Hashes.size() > 0
			? info.Hashes.get(0).hashCode() : NotificationUtil.MISSING_BOOK_ID;

		Uri uri = null;
		try {
			uri = Uri.parse(info.DownloadUrl);
		} catch (Exception e) {
		}
		builder.setAutoCancel(uri == null);
		if (uri != null) {
			final Intent downloadIntent = new Intent(myActivity, MissingBookActivity.class);
			downloadIntent
				.setData(uri)
				.putExtra(BookDownloaderService.Key.FROM_SYNC, true)
				.putExtra(BookDownloaderService.Key.BOOK_MIME, info.Mimetype)
				.putExtra(BookDownloaderService.Key.BOOK_KIND, UrlInfo.Type.Book)
				.putExtra(BookDownloaderService.Key.BOOK_TITLE, info.Title)
				.putExtra(BookDownloaderService.Key.NOTIFICATION_TO_DISMISS_ID, notificationId);
			builder.setContentIntent(PendingIntent.getActivity(myActivity, 0, downloadIntent, 0));
		} else {
			builder.setContentIntent(PendingIntent.getActivity(myActivity, 0, new Intent(), 0));
		}
		notificationManager.notify(notificationId, builder.build());
	}
}
