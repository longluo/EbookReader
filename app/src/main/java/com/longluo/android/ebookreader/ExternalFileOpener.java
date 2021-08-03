package com.longluo.android.ebookreader;

import java.math.BigInteger;
import java.util.Random;

import android.app.AlertDialog;
import android.content.*;

import com.longluo.zlibrary.core.options.Config;
import com.longluo.zlibrary.core.options.ZLStringOption;
import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.book.Book;
import com.longluo.ebookreader.book.Bookmark;
import com.longluo.ebookreader.fbreader.FBReaderApp;
import com.longluo.ebookreader.formats.ExternalFormatPlugin;

import com.longluo.android.ebookreader.api.FBReaderIntents;
import com.longluo.android.ebookreader.formatPlugin.PluginUtil;
import com.longluo.android.util.PackageUtil;

class ExternalFileOpener implements FBReaderApp.ExternalFileOpener {
	private final String myPluginCode = new BigInteger(80, new Random()).toString();
	private final FBReader myReader;
	private volatile AlertDialog myDialog;

	ExternalFileOpener(FBReader reader) {
		myReader = reader;
	}

	public void openFile(final ExternalFormatPlugin plugin, final Book book, Bookmark bookmark) {
		if (myDialog != null) {
			myDialog.dismiss();
			myDialog = null;
		}

		final Intent intent = PluginUtil.createIntent(plugin, FBReaderIntents.Action.PLUGIN_VIEW);
		FBReaderIntents.putBookExtra(intent, book);
		FBReaderIntents.putBookmarkExtra(intent, bookmark);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

		new ZLStringOption("PluginCode", plugin.packageName(), "").setValue(myPluginCode);
		intent.putExtra("PLUGIN_CODE", myPluginCode);

		Config.Instance().runOnConnect(new Runnable() {
			public void run() {
				try {
					myReader.startActivity(intent);
					myReader.overridePendingTransition(0, 0);
				} catch (ActivityNotFoundException e) {
					showErrorDialog(plugin, book);
				}
			}
		});
	}

	private void showErrorDialog(final ExternalFormatPlugin plugin, final Book book) {
		final ZLResource rootResource = ZLResource.resource("dialog");
		final ZLResource buttonResource = rootResource.getResource("button");
		final ZLResource dialogResource = rootResource.getResource("missingPlugin");
		final AlertDialog.Builder builder = new AlertDialog.Builder(myReader)
			.setTitle(dialogResource.getValue())
			.setMessage(dialogResource.getResource("message").getValue().replace("%s", plugin.supportedFileType()))
			.setPositiveButton(buttonResource.getResource("yes").getValue(), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					PackageUtil.installFromMarket(myReader, plugin.packageName());
					myDialog = null;
				}
			})
			.setNegativeButton(buttonResource.getResource("no").getValue(), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					myReader.onPluginNotFound(book);
					myDialog = null;
				}
			})
			.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					myReader.onPluginNotFound(book);
					myDialog = null;
				}
			});

		final Runnable showDialog = new Runnable() {
			public void run() {
				myDialog = builder.create();
				myDialog.show();
			}
		};
		if (!myReader.IsPaused) {
			myReader.runOnUiThread(showDialog);
		} else {
			myReader.OnResumeAction = showDialog;
		}
	}
}
