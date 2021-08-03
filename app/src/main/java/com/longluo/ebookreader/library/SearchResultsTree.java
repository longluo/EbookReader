package com.longluo.ebookreader.library;

import com.longluo.ebookreader.book.Book;
import com.longluo.ebookreader.book.Filter;
import com.longluo.util.Pair;
import com.longluo.zlibrary.core.resources.ZLResource;

public class SearchResultsTree extends FilteredTree {
    public final String Pattern;
    private final String myId;
    private final ZLResource myResource;

    SearchResultsTree(RootTree root, String id, String pattern, int position) {
        super(root, new Filter.ByPattern(pattern), position);
        myId = id;
        myResource = resource().getResource(myId);
        Pattern = pattern != null ? pattern : "";
    }

    @Override
    public String getName() {
        return myResource.getValue();
    }

    @Override
    public Pair<String, String> getTreeTitle() {
        return new Pair(getSummary(), null);
    }

    @Override
    protected String getStringId() {
        return myId;
    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    @Override
    public String getSummary() {
        return myResource.getResource("summary").getValue().replace("%s", Pattern);
    }

    @Override
    protected boolean createSubtree(Book book) {
        return createBookWithAuthorsSubtree(book);
    }
}
