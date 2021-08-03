package com.longluo.ebookreader.library;

import com.longluo.util.Pair;

import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.book.*;

public class FavoritesTree extends FilteredTree {
	private final ZLResource myResource;

	FavoritesTree(RootTree root) {
		super(root, new Filter.ByLabel(Book.FAVORITE_LABEL), -1);
		myResource = resource().getResource(ROOT_FAVORITES);
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
		return ROOT_FAVORITES;
	}

	@Override
	public boolean isSelectable() {
		return false;
	}

	@Override
	public Status getOpeningStatus() {
		return Collection.hasBooks(new Filter.ByLabel(Book.FAVORITE_LABEL))
			? Status.ALWAYS_RELOAD_BEFORE_OPENING
			: Status.CANNOT_OPEN;
	}

	@Override
	public String getOpeningStatusMessage() {
		return getOpeningStatus() == Status.CANNOT_OPEN
			? "noFavorites" : super.getOpeningStatusMessage();
	}

	@Override
	protected boolean createSubtree(Book book) {
		return createBookWithAuthorsSubtree(book);
	}
}
