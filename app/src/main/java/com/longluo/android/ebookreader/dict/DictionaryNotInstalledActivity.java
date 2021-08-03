package com.longluo.android.ebookreader.dict;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.longluo.zlibrary.core.resources.ZLResource;
import com.longluo.ebookreader.R;
import com.longluo.android.util.UIMessageUtil;
import com.longluo.android.util.PackageUtil;

public class DictionaryNotInstalledActivity extends ListActivity {
	static final String DICTIONARY_NAME_KEY = "fbreader.dictionary.name";
	static final String PACKAGE_NAME_KEY = "fbreader.package.name";

	private ZLResource myResource;
	private String myDictionaryName;
	private String myPackageName;

	@Override
	protected void onCreate(Bundle saved) {
		super.onCreate(saved);
		myResource = ZLResource.resource("dialog").getResource("missingDictionary");
		myDictionaryName = getIntent().getStringExtra(DICTIONARY_NAME_KEY);
		myPackageName = getIntent().getStringExtra(PACKAGE_NAME_KEY);
		setTitle(myResource.getValue().replaceAll("%s", myDictionaryName));
		final Adapter adapter = new Adapter();
		setListAdapter(adapter);
		getListView().setOnItemClickListener(adapter);
	}

	private final class Adapter extends BaseAdapter implements AdapterView.OnItemClickListener {
		private final String[] myItems = new String[] {
			"install",
			"configure",
			"skip"
		};

		public int getCount() {
			return myItems.length;
		}

		public String getItem(int position) {
			return myItems[position];
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, final ViewGroup parent) {
			final View view = convertView != null
				? convertView
				: LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
			final TextView titleView = (TextView)view.findViewById(R.id.menu_item_title);
			titleView.setText(
				myResource.getResource(myItems[position]).getValue().replaceAll("%s", myDictionaryName)
			);
			return view;
		}

		public final void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			switch (position) {
				case 0: // install
					installDictionary();
					break;
				case 1: // configure
					startActivity(new Intent(
						Intent.ACTION_VIEW, Uri.parse("fbreader-action:preferences#dictionary")
					));
					break;
				case 2: // skip
					break;
			}
			finish();
		}
	}

	private void installDictionary() {
		if (!PackageUtil.installFromMarket(this, myPackageName)) {
			UIMessageUtil.showErrorMessage(this, "cannotRunAndroidMarket", myDictionaryName);
		}
	}
}
