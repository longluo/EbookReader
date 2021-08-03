package com.longluo.android.util;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.EditText;

import com.longluo.zlibrary.core.resources.ZLResource;

public abstract class SearchDialogUtil {
	public static void showDialog(Activity activity, Class<? extends Activity> clazz, String initialPattern, DialogInterface.OnCancelListener listener) {
		showDialog(activity, clazz, initialPattern, listener, null);
	}

	public static void showDialog(final Activity activity, final Class<? extends Activity> clazz, final String initialPattern, DialogInterface.OnCancelListener listener, final Bundle bundle) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		builder.setTitle(ZLResource.resource("menu").getResource("search").getValue());

		final EditText input = new EditText(activity);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		input.setText(initialPattern);
		builder.setView(input);

		final ZLResource dialogResource = ZLResource.resource("dialog").getResource("button");
		builder.setPositiveButton(dialogResource.getResource("ok").getValue(), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.startActivity(
					new Intent(Intent.ACTION_SEARCH)
						.setClass(activity, clazz)
						.putExtra(SearchManager.QUERY, input.getText().toString())
						.putExtra(SearchManager.APP_DATA, bundle)
				);
			}
		});
		builder.setNegativeButton(dialogResource.getResource("cancel").getValue(), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		if (listener != null) {
			builder.setOnCancelListener(listener);
		}
		AlertDialog dialog = builder.create();
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		dialog.show();
	}
}
