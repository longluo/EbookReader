package com.longluo.zlibrary.ui.android.error;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;

import com.longluo.ebookreader.R;

public class BugReportActivity extends Activity implements ErrorKeys {
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.bug_report_view);
		final StringBuilder reportText = new StringBuilder();

		reportText.append("Package:").append(getPackageName()).append("\n");
		reportText.append("Model:").append(Build.MODEL).append("\n");
		reportText.append("Device:").append(Build.DEVICE).append("\n");
		reportText.append("Product:").append(Build.PRODUCT).append("\n");
		reportText.append("Manufacturer:").append(Build.MANUFACTURER).append("\n");
		reportText.append("Version:").append(Build.VERSION.RELEASE).append("\n");
		reportText.append(getIntent().getStringExtra(STACKTRACE));

		final TextView reportTextView = (TextView)findViewById(R.id.report_text);
		reportTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
		reportTextView.setClickable(false);
		reportTextView.setLongClickable(false);

		final String versionName = new ErrorUtil(this).getVersionName();
		reportTextView.append("FBReader " + versionName + " has been crached. You can send the report to developers.\n\n");
		reportTextView.append(reportText);

		findViewById(R.id.send_report).setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent sendIntent = new Intent(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "exception@fbreader.org" });
				sendIntent.putExtra(Intent.EXTRA_TEXT, reportText.toString());
				sendIntent.putExtra(Intent.EXTRA_SUBJECT, "FBReader " + versionName + " exception report");
				sendIntent.setType("message/rfc822");
				startActivity(sendIntent);
				finish();
			}
		});

		findViewById(R.id.cancel_report).setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				finish();
			}
		});
	}
}
