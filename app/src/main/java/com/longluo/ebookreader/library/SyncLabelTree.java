package com.longluo.ebookreader.library;

import com.longluo.util.Pair;

import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.book.Book;
import com.longluo.ebookreader.book.Filter;

public class SyncLabelTree extends FilteredTree {
	public final String Label;
	private final ZLResource myResource;

	SyncLabelTree(SyncTree parent, String label, Filter filter, ZLResource resource) {
		super(parent, filter, -1);
		Label = label;
		myResource = resource;
	}

	@Override
	public String getName() {
		return myResource.getValue();
	}

	@Override
	public Pair<String,String> getTreeTitle() {
		return new Pair(getSummary(), null);
	}

	@Override
	public String getSummary() {
		return myResource.getResource("summary").getValue();
	}

	@Override
	protected String getStringId() {
		return "@SyncLabelTree " + Label;
	}

	@Override
	public boolean isSelectable() {
		return false;
	}

	@Override
	public Status getOpeningStatus() {
		return Status.ALWAYS_RELOAD_BEFORE_OPENING;
	}

	@Override
	protected boolean createSubtree(Book book) {
		return createBookWithAuthorsSubtree(book);
	}
}
