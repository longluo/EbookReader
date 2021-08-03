package com.longluo.ebookreader.library;

import com.longluo.zlibrary.core.image.ZLImage;

import com.longluo.ebookreader.book.*;
import com.longluo.ebookreader.formats.PluginCollection;
import com.longluo.ebookreader.tree.FBTree;

public class BookTree extends LibraryTree {
	public final Book Book;

	BookTree(IBookCollection<Book> collection, PluginCollection pluginCollection, Book book) {
		super(collection, pluginCollection);
		Book = book;
	}

	BookTree(LibraryTree parent, Book book) {
		super(parent);
		Book = book;
	}

	BookTree(LibraryTree parent, Book book, int position) {
		super(parent, position);
		Book = book;
	}

	@Override
	public String getName() {
		return Book.getTitle();
	}

	@Override
	public String getSummary() {
		return "";
	}

	@Override
	public Book getBook() {
		return Book;
	}

	@Override
	protected String getStringId() {
		return "@BookTree " + getName();
	}

	@Override
	protected ZLImage createCover() {
		return CoverUtil.getCover(Book, PluginCollection);
	}

	@Override
	public boolean containsBook(Book book) {
		return Collection.sameBook(book, Book);
	}

	@Override
	protected String getSortKey() {
		return Book.getSortKey();
	}

	@Override
	public int compareTo(FBTree tree) {
		final int cmp = super.compareTo(tree);
		if (cmp == 0 && tree instanceof BookTree) {
			final Book b = ((BookTree)tree).Book;
			if (Book != null && b != null) {
				return Book.getPath().compareTo(b.getPath());
			}
		}
		return cmp;
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof BookTree)) {
			return false;
		}
		return Book.equals(((BookTree)object).Book);
	}
}
