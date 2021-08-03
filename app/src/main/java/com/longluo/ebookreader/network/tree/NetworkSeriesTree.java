package com.longluo.ebookreader.network.tree;

import java.util.*;

import com.longluo.zlibrary.core.image.ZLImage;

import com.longluo.ebookreader.tree.FBTree;
import com.longluo.ebookreader.network.*;


public class NetworkSeriesTree extends NetworkTree {

	public final String SeriesTitle;

	private final boolean myShowAuthors;

	NetworkSeriesTree(NetworkTree parent, String seriesTitle, int position, boolean showAuthors) {
		super(parent, position);
		SeriesTitle = seriesTitle;
		myShowAuthors = showAuthors;
	}

	@Override
	public String getName() {
		return SeriesTitle;
	}

	@Override
	public String getSummary() {
		if (!myShowAuthors) {
			return super.getSummary();
		}

		StringBuilder builder = new StringBuilder();
		int count = 0;

		Set<NetworkBookItem.AuthorData> authorSet = new TreeSet<NetworkBookItem.AuthorData>();
		for (FBTree tree : subtrees()) {
			if (!(tree instanceof NetworkBookTree)) {
				continue;
			}
			final NetworkBookItem book = ((NetworkBookTree)tree).Book;

			for (NetworkBookItem.AuthorData author : book.Authors) {
				if (!authorSet.contains(author)) {
					authorSet.add(author);
					if (count++ > 0) {
						builder.append(",  ");
					}
					builder.append(author.DisplayName);
					if (count == 5) {
						return builder.toString();
					}
				}
			}
		}
		return builder.toString();
	}

	@Override
	protected ZLImage createCover() {
		for (FBTree tree : subtrees()) {
			if (tree instanceof NetworkBookTree) {
				return ((NetworkBookTree)tree).createCover();
			}
		}
		return null;
	}

	@Override
	public void removeTrees(Set<NetworkTree> trees) {
		super.removeTrees(trees);
		if (subtrees().isEmpty()) {
			removeSelf();
		}
	}

	@Override
	protected String getStringId() {
		return "@Series:" + SeriesTitle;
	}
}
