package com.longluo.android.ebookreader.preferences.background;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.longluo.zlibrary.core.filesystem.ZLFile;
import com.longluo.zlibrary.core.resources.ZLResource;
import com.longluo.ebookreader.R;

import com.longluo.ebookreader.fbreader.WallpapersUtil;

public class PredefinedImages extends ListActivity implements AdapterView.OnItemClickListener {
	private final ZLResource myResource = ZLResource.resource("Preferences").getResource("colors").getResource("background");

	@Override
	protected void onStart() {
		super.onStart();
		setTitle(myResource.getValue());
		final ArrayAdapter<ZLFile> adapter = new ArrayAdapter<ZLFile>(
			this, R.layout.background_predefined_item, R.id.background_predefined_item_title
		) {
			public View getView(int position, View convertView, final ViewGroup parent) {
				final View view = super.getView(position, convertView, parent);

				final TextView titleView =
					(TextView)view.findViewById(R.id.background_predefined_item_title);
				final String name = getItem(position).getShortName();
				final String key = name.substring(0, name.indexOf("."));
				titleView.setText(myResource.getResource(key).getValue());

				final View previewWidget =
					view.findViewById(R.id.background_predefined_item_preview);
				try {
					previewWidget.setBackgroundDrawable(
						new BitmapDrawable(getResources(), getItem(position).getInputStream())
					);
				} catch (Throwable t) {
				}

				return view;
			}
		};
		for (ZLFile file : WallpapersUtil.predefinedWallpaperFiles()) {
			adapter.add(file);
		}
		setListAdapter(adapter);
		getListView().setOnItemClickListener(this);
	}

	public final void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		setResult(RESULT_OK, new Intent().putExtra(
			BackgroundPreference.VALUE_KEY,
			((ZLFile)getListAdapter().getItem(position)).getPath()
		));
		finish();
	}
}
