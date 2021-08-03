package com.longluo.ebookreader.library;

import java.util.Arrays;
import java.util.List;

import com.longluo.util.Pair;

import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.book.Book;
import com.longluo.ebookreader.book.Filter;

public class SyncTree extends FirstLevelTree {
	private final List<String> myLabels = Arrays.asList(
		Book.SYNCHRONISED_LABEL,
		Book.SYNC_FAILURE_LABEL,
		Book.SYNC_DELETED_LABEL
	);

	SyncTree(RootTree root) {
		super(root, ROOT_SYNC);
	}

	@Override
	public Pair<String,String> getTreeTitle() {
		return new Pair(getName(), null);
	}

	@Override
	public Status getOpeningStatus() {
		return Status.ALWAYS_RELOAD_BEFORE_OPENING;
	}

	@Override
	public void waitForOpening() {
		clear();

		final ZLResource baseResource = resource().getResource(ROOT_SYNC);
		Filter others = new Filter.HasPhysicalFile();

		for (String label : myLabels) {
			final Filter filter = new Filter.ByLabel(label);
			if (Collection.hasBooks(filter)) {
				new SyncLabelTree(this, label, filter, baseResource.getResource(label));
			}
			others = new Filter.And(others, new Filter.Not(filter));
		}
		if (Collection.hasBooks(others)) {
			new SyncLabelTree(
				this,
				Book.SYNC_TOSYNC_LABEL,
				others,
				baseResource.getResource(Book.SYNC_TOSYNC_LABEL)
			);
		}
	}
}
