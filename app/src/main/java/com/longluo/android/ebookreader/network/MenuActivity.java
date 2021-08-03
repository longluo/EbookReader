package com.longluo.android.ebookreader.network;

import java.util.*;

import android.app.ListActivity;
import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.longluo.ebookreader.R;

import com.longluo.android.ebookreader.api.PluginApi;

abstract class MenuActivity extends ListActivity implements AdapterView.OnItemClickListener {
	protected List<PluginApi.MenuActionInfo> myInfos;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		myInfos = new ArrayList<PluginApi.MenuActionInfo>();

		init();

		try {
			startActivityForResult(new Intent(getAction(), getIntent().getData()), 0);
		} catch (ActivityNotFoundException e) {
			if (finishInitialization()) {
				return;
			}
		}

		setListAdapter(new ActionListAdapter());
		getListView().setOnItemClickListener(this);
	}

	public final void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		runItem(myInfos.get(position));
		finish();
	}

	private boolean finishInitialization() {
		switch (myInfos.size()) {
			default:
				return false;
			case 0:
				finish();
				return true;
			case 1:
				runItem(myInfos.get(0));
				finish();
				return true;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (intent != null) {
			final List<PluginApi.MenuActionInfo> actions =
				intent.<PluginApi.MenuActionInfo>getParcelableArrayListExtra(
					PluginApi.PluginInfo.KEY
				);
			if (actions != null) {
				myInfos.addAll(actions);
			}
			if (finishInitialization()) {
				return;
			}
			Collections.sort(myInfos);
			((ActionListAdapter)getListAdapter()).notifyDataSetChanged();
			getListView().invalidateViews();
		}
	}

	protected abstract void init();
	protected abstract String getAction();
	protected abstract void runItem(final PluginApi.MenuActionInfo info);

	private class ActionListAdapter extends BaseAdapter {
		public final int getCount() {
			return myInfos.size();
		}

		public final PluginApi.MenuActionInfo getItem(int position) {
			return myInfos.get(position);
		}

		public final long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, final ViewGroup parent) {
			final View view = convertView != null
				? convertView
				: LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
			final TextView titleView = (TextView)view.findViewById(R.id.menu_item_title);
			titleView.setText(getItem(position).MenuItemName);
			return view;
		}
	}
}
