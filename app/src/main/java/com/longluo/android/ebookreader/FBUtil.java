package com.longluo.android.ebookreader;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;

import com.longluo.zlibrary.core.filesystem.ZLPhysicalFile;
import com.longluo.zlibrary.core.filetypes.FileTypeCollection;
import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.book.Book;
import com.longluo.ebookreader.book.BookUtil;

public abstract class FBUtil {
	public static void shareBook(Activity activity, Book book) {
		try {
			final ZLPhysicalFile file = BookUtil.fileByBook(book).getPhysicalFile();
			if (file == null) {
				// That should be impossible
				return;
			}
			final CharSequence sharedFrom =
				Html.fromHtml(ZLResource.resource("sharing").getResource("sharedFrom").getValue());
			activity.startActivity(
				new Intent(Intent.ACTION_SEND)
					.setType(FileTypeCollection.Instance.rawMimeType(file).Name)
					.putExtra(Intent.EXTRA_SUBJECT, book.getTitle())
					.putExtra(Intent.EXTRA_TEXT, sharedFrom)
					.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file.javaFile()))
			);
		} catch (ActivityNotFoundException e) {
			// TODO: show toast
		}
	}
}
