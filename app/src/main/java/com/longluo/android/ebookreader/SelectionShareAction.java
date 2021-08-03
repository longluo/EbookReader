package com.longluo.android.ebookreader;

import android.content.Intent;

import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.fbreader.FBReaderApp;
import com.longluo.ebookreader.fbreader.FBView;
import com.longluo.ebookreader.util.TextSnippet;

public class SelectionShareAction extends FBAndroidAction {
	SelectionShareAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	protected void run(Object ... params) {
		final FBView fbview = Reader.getTextView();
		final TextSnippet snippet = fbview.getSelectedSnippet();
		if (snippet == null) {
			return;
		}

		final String text = snippet.getText();
		final String title = Reader.getCurrentBook().getTitle();
		fbview.clearSelection();

		final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT,
			ZLResource.resource("selection").getResource("quoteFrom").getValue().replace("%s", title)
		);
		intent.putExtra(android.content.Intent.EXTRA_TEXT, text);
		BaseActivity.startActivity(Intent.createChooser(intent, null));
	}
}
