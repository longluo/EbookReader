package com.longluo.ebookreader.library;

import java.util.*;

import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.book.*;
import com.longluo.ebookreader.formats.PluginCollection;
import com.longluo.ebookreader.tree.FBTree;

public abstract class LibraryTree extends FBTree {
	public static ZLResource resource() {
		return ZLResource.resource("library");
	}

	public final IBookCollection<Book> Collection;
	public final PluginCollection PluginCollection;

	static final String ROOT_EXTERNAL_VIEW = "bookshelfView";
	static final String ROOT_FOUND = "found";
	static final String ROOT_FAVORITES = "favorites";
	static final String ROOT_RECENT = "recent";
	static final String ROOT_BY_AUTHOR = "byAuthor";
	static final String ROOT_BY_TITLE = "byTitle";
	static final String ROOT_BY_SERIES = "bySeries";
	static final String ROOT_BY_TAG = "byTag";
	static final String ROOT_SYNC = "sync";
	static final String ROOT_FILE = "fileTree";

	protected LibraryTree(IBookCollection collection, PluginCollection pluginCollection) {
		super();
		Collection = collection;
		PluginCollection = pluginCollection;
	}

	protected LibraryTree(LibraryTree parent) {
		super(parent);
		Collection = parent.Collection;
		PluginCollection = parent.PluginCollection;
	}

	protected LibraryTree(LibraryTree parent, int position) {
		super(parent, position);
		Collection = parent.Collection;
		PluginCollection = parent.PluginCollection;
	}

	public Book getBook() {
		return null;
	}

	public boolean containsBook(Book book) {
		return false;
	}

	public boolean isSelectable() {
		return true;
	}

	boolean createTagSubtree(Tag tag) {
		final TagTree temp = new TagTree(Collection, PluginCollection, tag);
		int position = Collections.binarySearch(subtrees(), temp);
		if (position >= 0) {
			return false;
		} else {
			new TagTree(this, tag, - position - 1);
			return true;
		}
	}

	boolean createBookWithAuthorsSubtree(Book book) {
		final BookWithAuthorsTree temp = new BookWithAuthorsTree(Collection, PluginCollection, book);
		int position = Collections.binarySearch(subtrees(), temp);
		if (position >= 0) {
			return false;
		} else {
			new BookWithAuthorsTree(this, book, - position - 1);
			return true;
		}
	}

	public boolean removeBook(Book book) {
		final LinkedList<FBTree> toRemove = new LinkedList<FBTree>();
		for (FBTree tree : this) {
			if (tree instanceof BookTree && ((BookTree)tree).Book.equals(book)) {
				toRemove.add(tree);
			}
		}
		for (FBTree tree : toRemove) {
			tree.removeSelf();
		}
		return !toRemove.isEmpty();
	}

	public boolean onBookEvent(BookEvent event, Book book) {
		switch (event) {
			default:
			case Added:
				return false;
			case Removed:
				return removeBook(book);
			case Updated:
			{
				boolean changed = false;
				for (FBTree tree : this) {
					if (tree instanceof BookTree) {
						final Book b = ((BookTree)tree).Book;
						if (b.equals(book)) {
							b.updateFrom(book);
							changed = true;
						}
					}
				}
				return changed;
			}
		}
	}

	@Override
	public int compareTo(FBTree tree) {
		final int cmp = super.compareTo(tree);
		if (cmp == 0) {
			return getClass().getSimpleName().compareTo(tree.getClass().getSimpleName());
		}
		return cmp;
	}
}
