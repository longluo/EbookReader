package com.longluo.android.ebookreader;

import android.app.Application;
import android.text.ClipboardManager;

import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.fbreader.FBReaderApp;
import com.longluo.ebookreader.fbreader.FBView;
import com.longluo.ebookreader.util.TextSnippet;

import com.longluo.android.util.UIMessageUtil;

public class SelectionCopyAction extends FBAndroidAction {
	SelectionCopyAction(FBReader baseActivity, FBReaderApp fbreader) {
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
		fbview.clearSelection();

		final ClipboardManager clipboard =
			(ClipboardManager)BaseActivity.getApplication().getSystemService(Application.CLIPBOARD_SERVICE);
		clipboard.setText(text);
		UIMessageUtil.showMessageText(
			BaseActivity,
			ZLResource.resource("selection").getResource("textInBuffer").getValue().replace("%s", clipboard.getText())
		);
	}
}
