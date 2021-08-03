package com.longluo.android.ebookreader.library;

import android.text.Html;
import android.view.*;
import android.widget.*;

import com.longluo.zlibrary.core.filesystem.ZLFile;

import com.longluo.ebookreader.R;

import com.longluo.ebookreader.library.*;
import com.longluo.ebookreader.book.Book;

import com.longluo.android.ebookreader.tree.TreeActivity;
import com.longluo.android.ebookreader.tree.TreeAdapter;
import com.longluo.android.ebookreader.covers.CoverManager;

import com.longluo.android.util.ViewUtil;

class LibraryTreeAdapter extends TreeAdapter {
	private CoverManager myCoverManager;

	LibraryTreeAdapter(LibraryActivity activity) {
		super(activity);
	}

	private View createView(View convertView, ViewGroup parent, LibraryTree tree) {
		final View view = (convertView != null) ? convertView :
			LayoutInflater.from(parent.getContext()).inflate(R.layout.library_tree_item, parent, false);

		final boolean unread =
			tree.getBook() != null && !tree.getBook().hasLabel(Book.READ_LABEL);

		final TextView nameView = ViewUtil.findTextView(view, R.id.library_tree_item_name);
		if (unread) {
			nameView.setText(Html.fromHtml("<b>" + tree.getName()));
		} else {
			nameView.setText(tree.getName());
		}

		final TextView summaryView = ViewUtil.findTextView(view, R.id.library_tree_item_childrenlist);
		if (unread) {
			summaryView.setText(Html.fromHtml("<b>" + tree.getSummary()));
		} else {
			summaryView.setText(tree.getSummary());
		}

		return view;
	}

	public View getView(int position, View convertView, final ViewGroup parent) {
		final LibraryTree tree = (LibraryTree)getItem(position);
		final View view = createView(convertView, parent, tree);
		if (getActivity().isTreeSelected(tree)) {
			view.setBackgroundColor(0xff555555);
		} else {
			view.setBackgroundColor(0);
		}

		if (myCoverManager == null) {
			view.measure(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			final int coverHeight = view.getMeasuredHeight();
			final TreeActivity activity = getActivity();
			myCoverManager = new CoverManager(activity, activity.ImageSynchronizer, coverHeight * 15 / 32, coverHeight);
			view.requestLayout();
		}

		final ImageView coverView = ViewUtil.findImageView(view, R.id.library_tree_item_icon);
		if (!myCoverManager.trySetCoverImage(coverView, tree)) {
			coverView.setImageResource(getCoverResourceId(tree));
		}

		return view;
	}

	private int getCoverResourceId(LibraryTree tree) {
		if (tree.getBook() != null) {
			return R.drawable.ic_list_library_book;
		} else if (tree instanceof ExternalViewTree) {
			return R.drawable.plugin_bookshelf;
		} else if (tree instanceof FavoritesTree) {
			return R.drawable.ic_list_library_favorites;
		} else if (tree instanceof RecentBooksTree || tree instanceof SyncTree) {
			return R.drawable.ic_list_library_recent;
		} else if (tree instanceof AuthorListTree) {
			return R.drawable.ic_list_library_authors;
		} else if (tree instanceof TitleListTree) {
			return R.drawable.ic_list_library_books;
		} else if (tree instanceof TagListTree) {
			return R.drawable.ic_list_library_tags;
		} else if (tree instanceof FileFirstLevelTree) {
			return R.drawable.ic_list_library_folder;
		} else if (tree instanceof SearchResultsTree) {
			return R.drawable.ic_list_library_search;
		} else if (tree instanceof FileTree) {
			final ZLFile file = ((FileTree)tree).getFile();
			if (file.isArchive()) {
				return R.drawable.ic_list_library_zip;
			} else if (file.isDirectory() && file.isReadable()) {
				return R.drawable.ic_list_library_folder;
			} else {
				return R.drawable.ic_list_library_permission_denied;
			}
		} else if (tree instanceof AuthorTree) {
			return R.drawable.ic_list_library_author;
		} else if (tree instanceof TagTree) {
			return R.drawable.ic_list_library_tag;
		}

		return R.drawable.ic_list_library_books;
	}
}
