package com.longluo.android.ebookreader.preferences;

import android.content.Context;
import android.preference.Preference;
import android.view.View;
import android.widget.TextView;

import yuku.ambilwarna.AmbilWarnaDialog;

import com.longluo.zlibrary.core.resources.ZLResource;
import com.longluo.zlibrary.core.util.ZLColor;

import com.longluo.ebookreader.R;
import com.longluo.zlibrary.ui.android.util.ZLAndroidColorUtil;

public abstract class ColorPreference extends Preference {
	protected ColorPreference(Context context) {
		super(context);
		setWidgetLayoutResource(R.layout.color_preference);
	}

	public abstract String getTitle();
	protected abstract ZLColor getSavedColor();
	protected abstract void saveColor(ZLColor color);

	@Override
	protected void onBindView(View view) {
		super.onBindView(view);

		((TextView)view.findViewById(R.id.color_preference_title)).setText(getTitle());
		view.findViewById(R.id.color_preference_widget).setBackgroundColor(
			ZLAndroidColorUtil.rgb(getSavedColor())
		);
	}

	@Override
	protected void onClick() {
		final ZLResource buttonResource = ZLResource.resource("dialog").getResource("button");
		new AmbilWarnaDialog(
			getContext(),
			ZLAndroidColorUtil.rgb(getSavedColor()),
			new AmbilWarnaDialog.OnAmbilWarnaListener() {
				@Override
				public void onOk(AmbilWarnaDialog dialog, int color) {
					if (!callChangeListener(color)) {
						return;
					}
					saveColor(new ZLColor(color));
					notifyChanged();
				}

				@Override
				public void onCancel(AmbilWarnaDialog dialog) {
				}
			},
			buttonResource.getResource("ok").getValue(),
			buttonResource.getResource("cancel").getValue()
		).show();
	}
}
