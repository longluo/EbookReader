package com.longluo.ebookreader.ui.adapter;

import com.longluo.ebookreader.model.bean.packages.SearchBookPackage;
import com.longluo.ebookreader.ui.adapter.view.SearchBookHolder;
import com.longluo.ebookreader.ui.base.adapter.BaseListAdapter;
import com.longluo.ebookreader.ui.base.adapter.IViewHolder;


public class SearchBookAdapter extends BaseListAdapter<SearchBookPackage.BooksBean> {
    @Override
    protected IViewHolder<SearchBookPackage.BooksBean> createViewHolder(int viewType) {
        return new SearchBookHolder();
    }
}
