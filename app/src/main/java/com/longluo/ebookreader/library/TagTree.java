package com.longluo.ebookreader.library;

import java.util.List;

import com.longluo.ebookreader.book.*;
import com.longluo.ebookreader.formats.PluginCollection;

public final class TagTree extends FilteredTree {
	public final Tag Tag;

	TagTree(IBookCollection collection, PluginCollection pluginCollection, Tag tag) {
		super(collection, pluginCollection, new Filter.ByTag(tag));
		Tag = tag;
	}

	TagTree(LibraryTree parent, Tag tag, int position) {
		super(parent, new Filter.ByTag(tag), position);
		Tag = tag;
	}

	@Override
	public String getName() {
		return Tag.NULL.equals(Tag) ? resource().getResource("booksWithNoTags").getValue() : Tag.Name;
	}

	@Override
	protected String getStringId() {
		return "@TagTree " + getName();
	}

	protected String getSortKey() {
		return Tag.NULL.equals(Tag) ? null : Tag.Name;
	}

	@Override
	public boolean containsBook(Book book) {
		if (book == null) {
			return false;
		}
		if (Tag.NULL.equals(Tag)) {
			return book.tags().isEmpty();
		}
		for (Tag t : book.tags()) {
			for (; t != null; t = t.Parent) {
				if (t == Tag) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void waitForOpening() {
		clear();
		if (!Tag.NULL.equals(Tag)) {
			for (Tag t : Collection.tags()) {
				if (Tag.equals(t.Parent)) {
					createTagSubtree(t);
				}
			}
		}
		createBookSubtrees();
	}

	@Override
	public boolean onBookEvent(BookEvent event, Book book) {
		switch (event) {
			case Added:
			{
				boolean changed = false;
				final List<Tag> bookTags = book.tags();
				if (bookTags.isEmpty()) {
					changed &= Tag.NULL.equals(Tag) && createBookWithAuthorsSubtree(book);
				} else {
					for (Tag t : bookTags) {
						if (Tag.equals(t)) {
							changed &= createBookWithAuthorsSubtree(book);
						} else if (Tag.equals(t.Parent)) {
							changed &= createTagSubtree(t);
						}
					}
				}
				return changed;
			}
			case Removed:
				// TODO: remove empty tag trees (?)
				return super.onBookEvent(event, book);
			case Updated:
			{
				// TODO: remove empty tag trees (?)
				boolean changed = removeBook(book);
				final List<Tag> bookTags = book.tags();
				if (bookTags.isEmpty()) {
					changed &= Tag.NULL.equals(Tag) && createBookWithAuthorsSubtree(book);
				} else {
					for (Tag t : bookTags) {
						if (Tag.equals(t)) {
							changed &= createBookWithAuthorsSubtree(book);
						} else if (Tag.equals(t.Parent)) {
							changed &= createTagSubtree(t);
						}
					}
				}
				return changed;
			}
			default:
				return super.onBookEvent(event, book);
		}
	}

	@Override
	protected boolean createSubtree(Book book) {
		return createBookWithAuthorsSubtree(book);
	}
}
