package com.longluo.ebookreader.library;

import com.longluo.zlibrary.core.resources.ZLResource;

public class ExternalViewTree extends LibraryTree {
	private final ZLResource myResource;

	ExternalViewTree(RootTree parent) {
		super(parent);
		myResource = resource().getResource(ROOT_EXTERNAL_VIEW);
	}

	@Override
	public String getName() {
		return myResource.getValue();
	}

	@Override
	protected String getStringId() {
		return ROOT_EXTERNAL_VIEW;
	}

	@Override
	public String getSummary() {
		return myResource.getResource("summary").getValue();
	}
}
