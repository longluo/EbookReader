package com.longluo.android.ebookreader.network;

import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

import com.longluo.ebookreader.R;

import com.longluo.ebookreader.network.*;
import com.longluo.ebookreader.network.tree.*;

import com.longluo.android.ebookreader.tree.TreeActivity;
import com.longluo.android.ebookreader.tree.TreeAdapter;
import com.longluo.android.ebookreader.covers.CoverManager;

import com.longluo.android.ebookreader.network.action.NetworkBookActions;

import com.longluo.android.util.ViewUtil;

class NetworkLibraryAdapter extends TreeAdapter {
	NetworkLibraryAdapter(NetworkLibraryActivity activity) {
		super(activity);
	}

	private CoverManager myCoverManager;

	public View getView(int position, View view, final ViewGroup parent) {
		final NetworkTree tree = (NetworkTree)getItem(position);
		if (tree == null) {
			throw new IllegalArgumentException("tree == null");
		}
		if (view == null) {
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_tree_item, parent, false);
			if (myCoverManager == null) {
				view.measure(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				final int coverHeight = view.getMeasuredHeight();
				final TreeActivity activity = getActivity();
				myCoverManager = new CoverManager(activity, activity.ImageSynchronizer, coverHeight * 15 / 32, coverHeight);
				view.requestLayout();
			}
		}

		ViewUtil.setSubviewText(view, R.id.library_tree_item_name, tree.getName());
		ViewUtil.setSubviewText(view, R.id.library_tree_item_childrenlist, tree.getSummary());
		setupCover(ViewUtil.findImageView(view, R.id.library_tree_item_icon), tree);

		final ImageView statusView = ViewUtil.findImageView(view, R.id.library_tree_item_status);
		final int status = (tree instanceof NetworkBookTree)
			? NetworkBookActions.getBookStatus(
				((NetworkBookTree)tree).Book,
				((NetworkLibraryActivity)getActivity()).BookCollection,
				((NetworkLibraryActivity)getActivity()).Connection
			  )
			: 0;
		if (status != 0) {
			statusView.setVisibility(View.VISIBLE);
			statusView.setImageResource(status);
		} else {
			statusView.setVisibility(View.GONE);
		}
		statusView.requestLayout();

		return view;
	}

	private void setupCover(final ImageView coverView, NetworkTree tree) {
		if (myCoverManager.trySetCoverImage(coverView, tree)) {
			return;
		}

		if (tree instanceof NetworkBookTree) {
			coverView.setImageResource(R.drawable.ic_list_library_book);
		} else if (tree instanceof SearchCatalogTree) {
			coverView.setImageResource(R.drawable.ic_list_library_search);
		} else if (tree instanceof RecentCatalogListTree) {
			coverView.setImageResource(R.drawable.ic_list_library_recent);
		} else if (tree instanceof BasketCatalogTree) {
			coverView.setImageResource(R.drawable.ic_list_library_basket);
		} else if (tree instanceof AddCustomCatalogItemTree) {
			coverView.setImageResource(R.drawable.ic_list_plus);
		} else if (tree instanceof ManageCatalogsItemTree) {
			coverView.setImageResource(R.drawable.ic_menu_filter);
		} else {
			coverView.setImageResource(R.drawable.ic_list_library_books);
		}
	}
}
