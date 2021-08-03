package com.longluo.ebookreader.library;

import java.util.List;

import com.longluo.ebookreader.book.IBookCollection;
import com.longluo.ebookreader.fbreader.options.SyncOptions;
import com.longluo.ebookreader.formats.PluginCollection;
import com.longluo.ebookreader.tree.FBTree;

public class RootTree extends LibraryTree {
	public RootTree(IBookCollection collection, PluginCollection pluginCollection) {
		super(collection, pluginCollection);

		//new ExternalViewTree(this);
		new FavoritesTree(this);
		new RecentBooksTree(this);
		new AuthorListTree(this);
		new TitleListTree(this);
		new SeriesListTree(this);
		new TagListTree(this);
		if (new SyncOptions().Enabled.getValue()) {
			new SyncTree(this);
		}
		new FileFirstLevelTree(this);
	}

	public LibraryTree getLibraryTree(LibraryTree.Key key) {
		if (key == null) {
			return null;
		}
		if (key.Parent == null) {
			return key.Id.equals(getUniqueKey().Id) ? this : null;
		}
		final LibraryTree parentTree = getLibraryTree(key.Parent);
		return parentTree != null ? (LibraryTree)parentTree.getSubtree(key.Id) : null;
	}

	public SearchResultsTree getSearchResultsTree() {
		return (SearchResultsTree)getSubtree(LibraryTree.ROOT_FOUND);
	}

	public SearchResultsTree createSearchResultsTree(String pattern) {
		final int position;
		final List<FBTree> children = subtrees();
		if (children.isEmpty()) {
			position = 0;
		} else {
			position = children.get(0) instanceof ExternalViewTree ? 1 : 0;
		}
		return new SearchResultsTree(this, LibraryTree.ROOT_FOUND, pattern, position);
	}

	@Override
	public String getName() {
		return resource().getValue();
	}

	@Override
	public String getSummary() {
		return resource().getValue();
	}

	@Override
	protected String getStringId() {
		return "@FBReaderLibraryRoot";
	}
}
