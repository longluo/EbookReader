package com.longluo.ebookreader.library;

import com.longluo.ebookreader.book.Book;

public class RecentBooksTree extends FirstLevelTree {
	RecentBooksTree(RootTree root) {
		super(root, ROOT_RECENT);
	}

	@Override
	public Status getOpeningStatus() {
		return Status.ALWAYS_RELOAD_BEFORE_OPENING;
	}

	@Override
	public void waitForOpening() {
		clear();
		for (Book book : Collection.recentlyOpenedBooks(12)) {
			new BookWithAuthorsTree(this, book);
		}
	}
}
