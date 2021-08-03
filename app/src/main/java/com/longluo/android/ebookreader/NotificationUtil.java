package com.longluo.android.ebookreader;

import android.app.NotificationManager;
import android.content.Context;

import com.longluo.zlibrary.core.filesystem.ZLPhysicalFile;
import com.longluo.ebookreader.book.Book;
import com.longluo.ebookreader.book.BookUtil;

public abstract class NotificationUtil {
	public static final int MISSING_BOOK_ID = 0x0fffffff;
	private static final int DOWNLOAD_ID_MIN = 0x10000000;
	private static final int DOWNLOAD_ID_MAX = 0x1fffffff;

	private NotificationUtil() {
	}

	public static int getDownloadId(String file) {
		return DOWNLOAD_ID_MIN + Math.abs(file.hashCode()) % (DOWNLOAD_ID_MAX - DOWNLOAD_ID_MIN + 1);
	}

	public static void drop(Context context, int id) {
		final NotificationManager notificationManager =
			(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(id);
	}

	public static void drop(Context context, Book book) {
		if (book == null) {
			return;
		}
		final ZLPhysicalFile file = BookUtil.fileByBook(book).getPhysicalFile();
		if (file == null) {
			return;
		}
		drop(context, getDownloadId(file.getPath()));
	}
}
