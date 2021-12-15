package com.longluo.ebookreader.model.flag;

import androidx.annotation.StringRes;

import com.longluo.ebookreader.ERApplication;
import com.longluo.ebookreader.R;


public enum BookListType {
    HOT(R.string.nb_fragment_book_list_hot, "last-seven-days"),
    NEWEST(R.string.nb_fragment_book_list_newest, "created"),
    COLLECT(R.string.nb_fragment_book_list_collect, "collectorCount");
    private final String typeName;
    private final String netName;

    BookListType(@StringRes int typeName, String netName) {
        this.typeName = ERApplication.getContext().getString(typeName);
        this.netName = netName;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getNetName() {
        return netName;
    }
}
