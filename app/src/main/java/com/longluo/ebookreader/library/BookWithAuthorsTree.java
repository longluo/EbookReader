package com.longluo.ebookreader.library;

import com.longluo.ebookreader.book.*;
import com.longluo.ebookreader.formats.PluginCollection;

public class BookWithAuthorsTree extends BookTree {
	BookWithAuthorsTree(IBookCollection collection, PluginCollection pluginCollection, Book book) {
		super(collection, pluginCollection, book);
	}

	BookWithAuthorsTree(LibraryTree parent, Book book) {
		super(parent, book);
	}

	BookWithAuthorsTree(LibraryTree parent, Book book, int position) {
		super(parent, book, position);
	}

	@Override
	public String getSummary() {
		StringBuilder builder = new StringBuilder();
		int count = 0;
		for (Author author : Book.authors()) {
			if (count++ > 0) {
				builder.append(",  ");
			}
			builder.append(author.DisplayName);
			if (count == 5) {
				break;
			}
		}
		return builder.toString();
	}
}
