package com.longluo.ebookreader.library;

import java.util.Collections;

import com.longluo.ebookreader.book.*;
import com.longluo.ebookreader.formats.PluginCollection;

public class AuthorTree extends FilteredTree {
	public final Author Author;

	AuthorTree(IBookCollection collection, PluginCollection pluginCollection, Author author) {
		super(collection, pluginCollection, new Filter.ByAuthor(author));
		Author = author;
	}

	AuthorTree(AuthorListTree parent, Author author, int position) {
		super(parent, new Filter.ByAuthor(author), position);
		Author = author;
	}

	@Override
	public String getName() {
		return Author.NULL.equals(Author)
			? resource().getResource("unknownAuthor").getValue() : Author.DisplayName;
	}

	@Override
	protected String getStringId() {
		return "@AuthorTree" + getSortKey();
	}

	@Override
	protected String getSortKey() {
		if (Author.NULL.equals(Author)) {
			return null;
		}
		return new StringBuilder()
			.append(" Author:")
			.append(Author.SortKey)
			.append(":")
			.append(Author.DisplayName)
			.toString();
	}

	private SeriesTree getSeriesSubtree(Series series) {
		final SeriesTree temp = new SeriesTree(Collection, PluginCollection, series, Author);
		int position = Collections.binarySearch(subtrees(), temp);
		if (position >= 0) {
			return (SeriesTree)subtrees().get(position);
		} else {
			return new SeriesTree(this, series, Author, - position - 1);
		}
	}

	@Override
	protected boolean createSubtree(Book book) {
		final SeriesInfo seriesInfo = book.getSeriesInfo();
		if (seriesInfo != null) {
			return getSeriesSubtree(seriesInfo.Series).createSubtree(book);
		}

		final BookTree temp = new BookTree(Collection, PluginCollection, book);
		int position = Collections.binarySearch(subtrees(), temp);
		if (position >= 0) {
			return false;
		} else {
			new BookTree(this, book, - position - 1);
			return true;
		}
	}
}
