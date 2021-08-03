package com.longluo.android.ebookreader.crash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.longluo.zlibrary.core.options.Config;
import com.longluo.zlibrary.core.options.ZLStringOption;
import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.Paths;

import com.longluo.ebookreader.R;

import com.longluo.android.ebookreader.FBReader;
import com.longluo.android.util.FileChooserUtil;

public class FixBooksDirectoryActivity extends Activity {
	private TextView myDirectoryView;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.books_directory_fix);

		final ZLResource resource = ZLResource.resource("crash").getResource("fixBooksDirectory");
		final ZLResource buttonResource = ZLResource.resource("dialog").getResource("button");

		final String title = resource.getResource("title").getValue();
		setTitle(title);

		final TextView textView = (TextView)findViewById(R.id.books_directory_fix_text);
		textView.setText(resource.getResource("text").getValue());

		myDirectoryView = (TextView)findViewById(R.id.books_directory_fix_directory);

		final View buttonsView = findViewById(R.id.books_directory_fix_buttons);
		final Button okButton = (Button)buttonsView.findViewById(R.id.ok_button);
		okButton.setText(buttonResource.getResource("ok").getValue());

		final View selectButton = findViewById(R.id.books_directory_fix_select_button);

		Config.Instance().runOnConnect(new Runnable() {
			public void run() {
				final ZLStringOption tempDirectoryOption = Paths.TempDirectoryOption(FixBooksDirectoryActivity.this);
				myDirectoryView.setText(tempDirectoryOption.getValue());
				selectButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						FileChooserUtil.runDirectoryChooser(
							FixBooksDirectoryActivity.this,
							1,
							title,
							tempDirectoryOption.getValue(),
							true
						);
					}
				});
				okButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						final String newDirectory = myDirectoryView.getText().toString();
						tempDirectoryOption.setValue(newDirectory);
						startActivity(new Intent(FixBooksDirectoryActivity.this, FBReader.class));
						finish();
					}
				});
			}
		});

		final Button cancelButton = (Button)buttonsView.findViewById(R.id.cancel_button);
		cancelButton.setText(buttonResource.getResource("cancel").getValue());
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) {
			myDirectoryView.setText(FileChooserUtil.folderPathFromData(data));
		}
	}
}
