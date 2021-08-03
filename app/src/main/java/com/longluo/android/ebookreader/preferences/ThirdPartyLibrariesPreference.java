package com.longluo.android.ebookreader.preferences;

import java.io.*;

import android.content.Context;
import android.preference.DialogPreference;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.longluo.zlibrary.core.filesystem.ZLFile;
import com.longluo.zlibrary.core.resources.ZLResource;

class ThirdPartyLibrariesPreference extends DialogPreference {
	ThirdPartyLibrariesPreference(Context context, ZLResource resource, String key) {
		super(context, null);

		setTitle(resource.getResource(key).getValue());
		setNegativeButtonText(null);
		setPositiveButtonText(ZLResource.resource("dialog").getResource("button").getResource("ok").getValue());
	}

	@Override
	protected View onCreateDialogView() {
		final TextView textView = new TextView(getContext());
		final StringBuilder html = new StringBuilder();
		try {
			final BufferedReader reader = new BufferedReader(new InputStreamReader(
				ZLFile.createFileByPath("data/licences.html").getInputStream()
			));
			String line;
			while ((line = reader.readLine()) != null) {
				html.append(line);
			}
			reader.close();
		} catch (IOException e) {
		}
		textView.setText(Html.fromHtml(html.toString()));
		textView.setPadding(10, 10, 10, 10);
		textView.setMovementMethod(new LinkMovementMethod());
		return textView;
	}
}
