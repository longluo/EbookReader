package com.longluo.ebookreader.library;

import java.util.List;

import com.longluo.zlibrary.core.util.MiscUtil;

import com.longluo.ebookreader.book.*;
import com.longluo.ebookreader.formats.PluginCollection;

abstract class FilteredTree extends LibraryTree {
	private final Filter myFilter;

	FilteredTree(IBookCollection collection, PluginCollection pluginCollection, Filter filter) {
		super(collection, pluginCollection);
		myFilter = filter;
	}

	FilteredTree(LibraryTree parent, Filter filter, int position) {
		super(parent, position);
		myFilter = filter;
	}

	@Override
	public String getSummary() {
		return MiscUtil.join(Collection.titles(new BookQuery(myFilter, 5)), ", ");
	}

	@Override
	public boolean containsBook(Book book) {
		return book != null && myFilter.matches(book);
	}

	@Override
	public Status getOpeningStatus() {
		return Status.ALWAYS_RELOAD_BEFORE_OPENING;
	}

	protected final void createBookSubtrees() {
		for (BookQuery query = new BookQuery(myFilter, 20); ; query = query.next()) {
			final List<Book> books = Collection.books(query);
			if (books.isEmpty()) {
				break;
			}
			for (Book b : books) {
				createSubtree(b);
			}
		}
	}

	@Override
	public void waitForOpening() {
		clear();
		createBookSubtrees();
	}

	@Override
	public boolean onBookEvent(BookEvent event, Book book) {
		switch (event) {
			case Added:
				return containsBook(book) && createSubtree(book);
			case Updated:
			{
				boolean changed = removeBook(book);
				changed |= containsBook(book) && createSubtree(book);
				return changed;
			}
			case Removed:
			default:
				return super.onBookEvent(event, book);
		}
	}

	protected abstract boolean createSubtree(Book book);
}
