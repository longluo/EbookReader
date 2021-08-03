package com.longluo.ebookreader.library;

import java.util.Collections;
import java.util.List;

import com.longluo.ebookreader.book.*;

public class AuthorListTree extends FirstLevelTree {
	AuthorListTree(RootTree root) {
		super(root, ROOT_BY_AUTHOR);
	}

	@Override
	public Status getOpeningStatus() {
		return Status.ALWAYS_RELOAD_BEFORE_OPENING;
	}

	@Override
	public void waitForOpening() {
		clear();
		for (Author a : Collection.authors()) {
			createAuthorSubtree(a);
		}
	}

	@Override
	public boolean onBookEvent(BookEvent event, Book book) {
		switch (event) {
			case Added:
			case Updated:
			{
				// TODO: remove empty authors tree after update (?)
				final List<Author> bookAuthors = book.authors();
				boolean changed = false;
				if (bookAuthors.isEmpty()) {
					changed &= createAuthorSubtree(Author.NULL);
				} else for (Author a : bookAuthors) {
					changed &= createAuthorSubtree(a);
				}
				return changed;
			}
			case Removed:
				// TODO: remove empty authors tree (?)
				return false;
			default:
				return false;
		}
	}

	private boolean createAuthorSubtree(Author author) {
		final AuthorTree temp = new AuthorTree(Collection, PluginCollection, author);
		int position = Collections.binarySearch(subtrees(), temp);
		if (position >= 0) {
			return false;
		} else {
			new AuthorTree(this, author, - position - 1);
			return true;
		}
	}
}
