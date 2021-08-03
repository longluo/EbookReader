package com.longluo.android.ebookreader.error;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.zlibrary.ui.android.error.ErrorKeys;
import com.longluo.zlibrary.ui.android.error.ErrorUtil;
import com.longluo.android.ebookreader.util.SimpleDialogActivity;

public class BookReadingErrorActivity extends SimpleDialogActivity implements ErrorKeys {
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		final ZLResource resource = ZLResource.resource("error").getResource("bookReading");
		setTitle(resource.getResource("title").getValue());

		textView().setText(getIntent().getStringExtra(MESSAGE));

		okButton().setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final Intent sendIntent = new Intent(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "issues@fbreader.org" });
				sendIntent.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra(STACKTRACE));
				sendIntent.putExtra(Intent.EXTRA_SUBJECT, "FBReader " + new ErrorUtil(BookReadingErrorActivity.this).getVersionName() + " book reading issue report");
				sendIntent.setType("message/rfc822");
				startActivity(sendIntent);
				finish();
			}
		});
		cancelButton().setOnClickListener(finishListener());
		setButtonTexts("sendReport", "cancel");
	}
}
