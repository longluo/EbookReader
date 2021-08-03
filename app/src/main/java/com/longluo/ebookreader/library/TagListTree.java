package com.longluo.ebookreader.library;

import java.util.List;

import com.longluo.ebookreader.book.*;

public class TagListTree extends FirstLevelTree {
	TagListTree(RootTree root) {
		super(root, ROOT_BY_TAG);
	}

	@Override
	public Status getOpeningStatus() {
		return Status.ALWAYS_RELOAD_BEFORE_OPENING;
	}

	@Override
	public void waitForOpening() {
		clear();
		for (Tag t : Collection.tags()) {
			if (t.Parent == null) {
				createTagSubtree(t);
			}
		}
	}

	@Override
	public boolean onBookEvent(BookEvent event, Book book) {
		switch (event) {
			case Added:
			case Updated:
			{
				// TODO: remove empty tag trees after update (?)
				final List<Tag> bookTags = book.tags();
				boolean changed = false;
				if (bookTags.isEmpty()) {
					changed &= createTagSubtree(Tag.NULL);
				} else for (Tag t : bookTags) {
					if (t.Parent == null) {
						changed &= createTagSubtree(t);
					}
				}
				return changed;
			}
			case Removed:
				// TODO: remove empty tag trees (?)
				return false;
			default:
				return false;
		}
	}
}
