package com.longluo.ebookreader.library;

import java.math.BigDecimal;

import com.longluo.ebookreader.book.Book;
import com.longluo.ebookreader.book.IBookCollection;
import com.longluo.ebookreader.formats.PluginCollection;
import com.longluo.ebookreader.tree.FBTree;

public final class BookInSeriesTree extends BookTree {
	BookInSeriesTree(IBookCollection collection, PluginCollection pluginCollection, Book book) {
		super(collection, pluginCollection, book);
	}

	BookInSeriesTree(LibraryTree parent, Book book, int position) {
		super(parent, book, position);
	}

	@Override
	public int compareTo(FBTree tree) {
		if (tree instanceof BookInSeriesTree) {
			final BigDecimal index0 = Book.getSeriesInfo().Index;
			final BigDecimal index1 = ((BookTree)tree).Book.getSeriesInfo().Index;
			final int cmp;
			if (index0 == null) {
				cmp = index1 == null ? 0 : 1;
			} else {
				cmp = index1 == null ? -1 : index0.compareTo(index1);
			}
			if (cmp != 0) {
				return cmp;
			}
		}
		return super.compareTo(tree);
	}
}
