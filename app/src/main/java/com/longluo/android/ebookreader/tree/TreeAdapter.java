package com.longluo.android.ebookreader.tree;

import java.util.*;

import android.widget.BaseAdapter;

import com.longluo.ebookreader.tree.FBTree;

public abstract class TreeAdapter extends BaseAdapter {
	private final TreeActivity myActivity;
	private final List<FBTree> myItems;

	protected TreeAdapter(TreeActivity activity) {
		myActivity = activity;
		myItems = Collections.synchronizedList(new ArrayList<FBTree>());
		activity.setListAdapter(this);
	}

	protected TreeActivity getActivity() {
		return myActivity;
	}

	public void remove(final FBTree item) {
		myActivity.runOnUiThread(new Runnable() {
			public void run() {
				myItems.remove(item);
				notifyDataSetChanged();
			}
		});
	}

	public void add(final FBTree item) {
		myActivity.runOnUiThread(new Runnable() {
			public void run() {
				myItems.add(item);
				notifyDataSetChanged();
			}
		});
	}

	public void add(final int index, final FBTree item) {
		myActivity.runOnUiThread(new Runnable() {
			public void run() {
				myItems.add(index, item);
				notifyDataSetChanged();
			}
		});
	}

	public void replaceAll(final Collection<FBTree> items, final boolean invalidateViews) {
		myActivity.runOnUiThread(new Runnable() {
			public void run() {
				synchronized (myItems) {
					myItems.clear();
					myItems.addAll(items);
				}
				notifyDataSetChanged();
				if (invalidateViews) {
					myActivity.getListView().invalidateViews();
				}
			}
		});
	}

	public int getCount() {
		return myItems.size();
	}

	public FBTree getItem(int position) {
		return myItems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getIndex(FBTree item) {
		return myItems.indexOf(item);
	}

	public FBTree getFirstSelectedItem() {
		synchronized (myItems) {
			for (FBTree t : myItems) {
				if (myActivity.isTreeSelected(t)) {
					return t;
				}
			}
		}
		return null;
	}
}
