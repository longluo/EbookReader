package com.longluo.android.ebookreader.network;

import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.net.Uri;
import android.content.Intent;

import com.longluo.zlibrary.core.util.MimeType;
import com.longluo.ebookreader.network.urlInfo.BookUrlInfo;

public class BookDownloader extends Activity {
	public static boolean acceptsUri(Uri uri, String mime) {
		final List<String> path = uri.getPathSegments();
		if (path == null || path.isEmpty()) {
			return false;
		}

		final String scheme = uri.getScheme();
		if ("epub".equals(scheme) || "book".equals(scheme)) {
			return true;
		}

		if (mime != null && Arrays.asList(new String[] {
			"application/epub+zip",
			"application/x-pilot-prc",
			"application/x-mobipocket-ebook",
			"application/x-fictionbook+xml",
			"application/x-fictionbook",
			"application/pdf",
			"application/x-pdf",
			"application/djvu",
			"application/x-djvu",
			"application/x-cbr",
			"application/x-cbz",
			"image/vnd.djvu",
			"image/x-djvu"
		}).contains(mime)) {
			return true;
		}

		final String fileName = path.get(path.size() - 1).toLowerCase();
		return
			fileName.endsWith(".fb2.zip") ||
			fileName.endsWith(".fb2") ||
			fileName.endsWith(".epub") ||
			fileName.endsWith(".oeb") ||
			fileName.endsWith(".mobi") ||
			fileName.endsWith(".txt") ||
			fileName.endsWith(".rtf") ||
			fileName.endsWith(".pdf") ||
			fileName.endsWith(".djvu") ||
			fileName.endsWith(".cbr") ||
			fileName.endsWith(".cbz") ||
			fileName.endsWith(".prc");
	}

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Thread.setDefaultUncaughtExceptionHandler(new com.longluo.zlibrary.ui.android.library.UncaughtExceptionHandler(this));

		final Intent intent = getIntent();
		Uri uri = intent.getData();
		intent.setData(null);
		if (uri == null || !acceptsUri(uri, intent.getType())) {
			finish();
			return;
		}

		if (!intent.hasExtra(BookDownloaderService.Key.SHOW_NOTIFICATIONS)) {
			intent.putExtra(BookDownloaderService.Key.SHOW_NOTIFICATIONS,
				BookDownloaderService.Notifications.ALREADY_IN_PROGRESS);
		}
		if ("epub".equals(uri.getScheme())) {
			uri = uri.buildUpon().scheme("http").build();
			intent.putExtra(BookDownloaderService.Key.BOOK_MIME, MimeType.APP_EPUB_ZIP.toString());
		}

		startService(
			new Intent(Intent.ACTION_DEFAULT, uri, this, BookDownloaderService.class)
				.putExtras(intent.getExtras())
		);
		finish();
	}
}
