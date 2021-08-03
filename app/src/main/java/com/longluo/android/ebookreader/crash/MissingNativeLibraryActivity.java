package com.longluo.android.ebookreader.crash;

import android.os.Bundle;

import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.android.ebookreader.util.SimpleDialogActivity;

public class MissingNativeLibraryActivity extends SimpleDialogActivity {
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		final ZLResource resource = ZLResource.resource("crash").getResource("missingNativeLibrary");

		setTitle(resource.getResource("title").getValue());
		textView().setText(resource.getResource("text").getValue());
		okButton().setOnClickListener(finishListener());
		setButtonTexts("ok", null);
	}
}
