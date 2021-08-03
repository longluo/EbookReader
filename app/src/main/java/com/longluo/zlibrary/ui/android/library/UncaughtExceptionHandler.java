package com.longluo.zlibrary.ui.android.library;

import java.io.*;

import android.app.Activity;
import android.content.*;
import android.net.Uri;
import android.os.Process;

import com.longluo.zlibrary.ui.android.error.BugReportActivity;
import com.longluo.android.ebookreader.api.FBReaderIntents;

public class UncaughtExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {
	private final Context myContext;

	public UncaughtExceptionHandler(Context context) {
		myContext = context;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable exception) {
		final StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));
		System.err.println(stackTrace);

		Intent intent = new Intent(
			FBReaderIntents.Action.CRASH,
			new Uri.Builder().scheme(exception.getClass().getSimpleName()).build()
		);
		intent.setPackage(FBReaderIntents.DEFAULT_PACKAGE);
		try {
			myContext.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			intent = new Intent(myContext, BugReportActivity.class);
			intent.putExtra(BugReportActivity.STACKTRACE, stackTrace.toString());
			myContext.startActivity(intent);
		}

		if (myContext instanceof Activity) {
			((Activity)myContext).finish();
		}

		Process.killProcess(Process.myPid());
		System.exit(10);
	}
}
