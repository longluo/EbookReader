package com.longluo.android.ebookreader;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.view.*;

import com.longluo.zlibrary.core.options.Config;

import com.longluo.ebookreader.R;

import com.longluo.ebookreader.fbreader.options.CancelMenuHelper;

import com.longluo.android.ebookreader.api.FBReaderIntents;
import com.longluo.android.ebookreader.libraryService.BookCollectionShadow;

import com.longluo.android.util.ViewUtil;

public class CancelActivity extends ListActivity {
	private BookCollectionShadow myCollection;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// we use this local variable to be sure collection is not null inside the runnable
		final BookCollectionShadow collection = new BookCollectionShadow();
		myCollection = collection;
		collection.bindToService(this, new Runnable() {
			public void run() {
				final ActionListAdapter adapter = new ActionListAdapter(
					new CancelMenuHelper().getActionsList(collection)
				);
				setListAdapter(adapter);
				getListView().setOnItemClickListener(adapter);
			}
		});
	}

	@Override
	protected void onStop() {
		if (myCollection != null) {
			myCollection.unbind();
			myCollection = null;
		}
		super.onStop();
	}

	private class ActionListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
		private final List<CancelMenuHelper.ActionDescription> myActions;

		ActionListAdapter(List<CancelMenuHelper.ActionDescription> actions) {
			myActions = actions;
		}

		public final int getCount() {
			return myActions.size();
		}

		public final CancelMenuHelper.ActionDescription getItem(int position) {
			return myActions.get(position);
		}

		public final long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, final ViewGroup parent) {
			final View view = convertView != null
				? convertView
				: LayoutInflater.from(parent.getContext()).inflate(R.layout.cancel_item, parent, false);
			final CancelMenuHelper.ActionDescription item = getItem(position);
			final TextView titleView = ViewUtil.findTextView(view, R.id.cancel_item_title);
			final TextView summaryView = ViewUtil.findTextView(view, R.id.cancel_item_summary);
			final String title = item.Title;
			final String summary = item.Summary;
			titleView.setText(title);
			if (summary != null) {
				summaryView.setVisibility(View.VISIBLE);
				summaryView.setText(summary);
				titleView.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
				));
			} else {
				summaryView.setVisibility(View.GONE);
				titleView.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT
				));
			}
			return view;
		}

		public final void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			final Intent data = new Intent();
			final CancelMenuHelper.ActionDescription item = getItem(position);
			data.putExtra(FBReaderIntents.Key.TYPE, item.Type.name());
			if (item instanceof CancelMenuHelper.BookmarkDescription) {
				FBReaderIntents.putBookmarkExtra(
					data, ((CancelMenuHelper.BookmarkDescription)item).Bookmark
				);
			}
			setResult(RESULT_FIRST_USER, data);
			finish();
		}
	}
}
