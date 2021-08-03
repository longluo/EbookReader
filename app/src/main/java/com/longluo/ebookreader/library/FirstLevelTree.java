package com.longluo.ebookreader.library;

import com.longluo.util.Pair;

import com.longluo.zlibrary.core.resources.ZLResource;

abstract class FirstLevelTree extends LibraryTree {
	private final String myId;
	private final ZLResource myResource;

	FirstLevelTree(RootTree root, int position, String id) {
		super(root, position);
		myId = id;
		myResource = resource().getResource(myId);
	}

	FirstLevelTree(RootTree root, String id) {
		super(root);
		myId = id;
		myResource = resource().getResource(myId);
	}

	@Override
	public String getName() {
		return myResource.getValue();
	}

	@Override
	public Pair<String,String> getTreeTitle() {
		return new Pair(getSummary(), null);
	}

	@Override
	public String getSummary() {
		return myResource.getResource("summary").getValue();
	}

	@Override
	protected String getStringId() {
		return myId;
	}

	@Override
	public boolean isSelectable() {
		return false;
	}
}
