package com.longluo.android.ebookreader;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.*;
import android.view.*;

import com.longluo.zlibrary.core.filesystem.ZLFile;
import com.longluo.zlibrary.core.resources.ZLResource;
import com.longluo.zlibrary.core.util.XmlUtil;
import com.longluo.ebookreader.R;

import com.longluo.android.util.PackageUtil;
import com.longluo.android.util.ViewUtil;

public class PluginListActivity extends ListActivity {
	private final ZLResource myResource = ZLResource.resource("plugins");

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setTitle(myResource.getValue());
		final PluginListAdapter adapter = new PluginListAdapter();
		setListAdapter(adapter);
		getListView().setOnItemClickListener(adapter);
	}

	private static class Plugin {
		final String Id;
		final String PackageName;

		Plugin(String id, String packageName) {
			Id = id;
			PackageName = packageName;
		}
	}

	private class Reader extends DefaultHandler {
		final PackageManager myPackageManager = getPackageManager();
		final List<Plugin> myPlugins;

		Reader(List<Plugin> plugins) {
			myPlugins = plugins;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			if ("plugin".equals(localName)) {
				try {
					if (Integer.valueOf(attributes.getValue("min-api")) > Build.VERSION.SDK_INT) {
						return;
					}
				} catch (Throwable t) {
					// ignore
				}
				final String id = attributes.getValue("id");
				final String packageName = attributes.getValue("package");
				try {
      				myPackageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
				} catch (PackageManager.NameNotFoundException e) {
					myPlugins.add(new Plugin(id, packageName));
				}
			}
		}
	}

	private class PluginListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
		private final List<Plugin> myPlugins = new LinkedList<Plugin>();

		PluginListAdapter() {
			XmlUtil.parseQuietly(
				ZLFile.createFileByPath("default/plugins.xml"),
				new Reader(myPlugins)
			);
		}

		public final int getCount() {
			return myPlugins.isEmpty() ? 1 : myPlugins.size();
		}

		public final Plugin getItem(int position) {
			return myPlugins.isEmpty() ? null : myPlugins.get(position);
		}

		public final long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, final ViewGroup parent) {
			final View view = convertView != null
				? convertView
				: LayoutInflater.from(parent.getContext()).inflate(R.layout.plugin_item, parent, false);
			final ImageView iconView = (ImageView)view.findViewById(R.id.plugin_item_icon);
			final TextView titleView = ViewUtil.findTextView(view, R.id.plugin_item_title);
			final TextView summaryView = ViewUtil.findTextView(view, R.id.plugin_item_summary);
			final Plugin plugin = getItem(position);
			if (plugin != null) {
				final ZLResource resource = myResource.getResource(plugin.Id);
				titleView.setText(resource.getValue());
				summaryView.setText(resource.getResource("summary").getValue());
				int iconId = R.drawable.fbreader;
				try {
					final Field f = R.drawable.class.getField("plugin_" + plugin.Id);
					iconId = f.getInt(R.drawable.class);
				} catch (Throwable t) {
					t.printStackTrace();
				}
				iconView.setImageResource(iconId);
			} else {
				final ZLResource resource = myResource.getResource("noMorePlugins");
				titleView.setText(resource.getValue());
				summaryView.setVisibility(View.GONE);
				iconView.setVisibility(View.GONE);
			}
			return view;
		}

		public final void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
			final Plugin plugin = getItem(position);
			if (plugin != null) {
				runOnUiThread(new Runnable() {
					public void run() {
						finish();
						PackageUtil.installFromMarket(PluginListActivity.this, plugin.PackageName);
					}
				});
			}
		}
	}
}
