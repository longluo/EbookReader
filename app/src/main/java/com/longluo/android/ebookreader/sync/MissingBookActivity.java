package com.longluo.android.ebookreader.sync;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.android.ebookreader.network.BookDownloaderService;
import com.longluo.android.ebookreader.util.SimpleDialogActivity;

public class MissingBookActivity extends SimpleDialogActivity {
	public static String errorTitle() {
		return ZLResource.resource("errorMessage").getResource("bookIsMissingTitle").getValue();
	}

	public static String errorMessage(String title) {
		return ZLResource.resource("errorMessage").getResource("bookIsMissing").getValue()
			.replace("%s", title);
	}

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		final Intent intent = getIntent();
		final String title = intent.getStringExtra(BookDownloaderService.Key.BOOK_TITLE);
		setTitle(errorTitle());
		textView().setText(errorMessage(title));
		intent.setClass(this, BookDownloaderService.class);

		okButton().setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startService(intent);
				finish();
			}
		});
		setButtonTexts("download", null);
	}
}
