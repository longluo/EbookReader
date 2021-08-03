package com.longluo.ebookreader.library;

import java.util.Collections;

import com.longluo.ebookreader.book.*;
import com.longluo.ebookreader.formats.PluginCollection;

public final class SeriesTree extends FilteredTree {
	public final Series Series;

	private static Filter filter(Series series, Author author) {
		final Filter f = new Filter.BySeries(series);
		return author != null ? new Filter.And(f, new Filter.ByAuthor(author)) : f;
	}

	SeriesTree(IBookCollection collection, PluginCollection pluginCollection, Series series, Author author) {
		super(collection, pluginCollection, filter(series, author));
		Series = series;
	}

	SeriesTree(LibraryTree parent, Series series, Author author, int position) {
		super(parent, filter(series, author), position);
		Series = series;
	}

	@Override
	public String getName() {
		return Series.getTitle();
	}

	@Override
	protected String getStringId() {
		return "@SeriesTree " + getName();
	}

	@Override
	protected String getSortKey() {
		return Series.getSortKey();
	}

	@Override
	protected boolean createSubtree(Book book) {
		final BookInSeriesTree temp = new BookInSeriesTree(Collection, PluginCollection, book);
		int position = Collections.binarySearch(subtrees(), temp);
		if (position >= 0) {
			return false;
		} else {
			new BookInSeriesTree(this, book, - position - 1);
			return true;
		}
	}
}
