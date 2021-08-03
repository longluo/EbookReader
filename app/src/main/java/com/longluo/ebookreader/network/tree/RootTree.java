package com.longluo.ebookreader.network.tree;

import com.longluo.ebookreader.network.*;

public final class RootTree extends NetworkTree {
	public final boolean IsFake;

	private final String myId;

	public RootTree(NetworkLibrary library, String id, boolean isFake) {
		super(library);
		IsFake = isFake;
		myId = id;
	}

	@Override
	public String getName() {
		return NetworkLibrary.resource().getValue();
	}

	@Override
	public String getSummary() {
		return NetworkLibrary.resource().getValue();
	}

	@Override
	protected String getStringId() {
		return myId;
	}
}
