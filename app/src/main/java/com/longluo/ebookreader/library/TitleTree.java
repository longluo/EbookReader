package com.longluo.ebookreader.library;

import com.longluo.ebookreader.book.*;
import com.longluo.ebookreader.formats.PluginCollection;

public final class TitleTree extends FilteredTree {
	public final String Prefix;

	TitleTree(IBookCollection collection, PluginCollection pluginCollection, String prefix) {
		super(collection, pluginCollection, new Filter.ByTitlePrefix(prefix));
		Prefix = prefix;
	}

	TitleTree(LibraryTree parent, String prefix, int position) {
		super(parent, new Filter.ByTitlePrefix(prefix), position);
		Prefix = prefix;
	}

	@Override
	public String getName() {
		return Prefix;
	}

	@Override
	protected String getStringId() {
		return "@PrefixTree " + getName();
	}

	@Override
	protected boolean createSubtree(Book book) {
		return createBookWithAuthorsSubtree(book);
	}
}
