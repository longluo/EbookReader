package com.longluo.ebookreader.library;

import java.util.List;

import com.longluo.util.Pair;

import com.longluo.zlibrary.core.filesystem.ZLFile;
import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.Paths;

public class FileFirstLevelTree extends FirstLevelTree {
	FileFirstLevelTree(RootTree root) {
		super(root, ROOT_FILE);
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
		for (String dir : Paths.BookPathOption.getValue()) {
			addChild(dir, resource().getResource("fileTreeLibrary").getValue(), dir);
		}
		addChild("/", "fileTreeRoot");
		final List<String> cards = Paths.allCardDirectories();
		if (cards.size() == 1) {
			addChild(cards.get(0), "fileTreeCard");
		} else {
			final ZLResource res = resource().getResource("fileTreeCard");
			final String title = res.getResource("withIndex").getValue();
			final String summary = res.getResource("summary").getValue();
			int index = 0;
			for (String dir : cards) {
				addChild(dir, title.replaceAll("%s", String.valueOf(++index)), summary);
			}
		}
	}

	private void addChild(String path, String title, String summary) {
		final ZLFile file = ZLFile.createFileByPath(path);
		if (file != null) {
			new FileTree(this, file, title, summary);
		}
	}

	private void addChild(String path, String resourceKey) {
		final ZLResource resource = resource().getResource(resourceKey);
		addChild(path, resource.getValue(), resource.getResource("summary").getValue());
	}
}
