package com.longluo.ebookreader.ui.adapter;

import com.longluo.ebookreader.model.bean.BookSortBean;
import com.longluo.ebookreader.ui.adapter.view.BookSortHolder;
import com.longluo.ebookreader.ui.base.adapter.BaseListAdapter;
import com.longluo.ebookreader.ui.base.adapter.IViewHolder;


public class BookSortAdapter extends BaseListAdapter<BookSortBean> {

    @Override
    protected IViewHolder<BookSortBean> createViewHolder(int viewType) {
        return new BookSortHolder();
    }
}
