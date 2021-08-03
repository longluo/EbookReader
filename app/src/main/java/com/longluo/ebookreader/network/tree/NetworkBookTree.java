package com.longluo.ebookreader.network.tree;

import com.longluo.zlibrary.core.image.ZLImage;

import com.longluo.ebookreader.network.*;

public class NetworkBookTree extends NetworkTree {
	public final NetworkBookItem Book;

	private final boolean myShowAuthors;

	public NetworkBookTree(NetworkTree parent, NetworkBookItem book, boolean showAuthors) {
		super(parent);
		Book = book;
		myShowAuthors = showAuthors;
	}

	NetworkBookTree(NetworkTree parent, NetworkBookItem book, int position, boolean showAuthors) {
		super(parent, position);
		Book = book;
		myShowAuthors = showAuthors;
	}

	@Override
	protected boolean canUseParentCover() {
		return false;
	}

	@Override
	public String getName() {
		return Book.Title.toString();
	}

	@Override
	public String getSummary() {
		if (!myShowAuthors && Book.Authors.size() < 2) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		int count = 0;
		for (NetworkBookItem.AuthorData author: Book.Authors) {
			if (count++ > 0) {
				builder.append(",  ");
			}
			builder.append(author.DisplayName);
		}
		return builder.toString();
	}

	@Override
	protected ZLImage createCover() {
		return createCoverForItem(Library, Book, true);
	}

	@Override
	protected String getStringId() {
		return Book.getStringId();
	}
}
