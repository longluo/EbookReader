package com.longluo.ebookreader.ui.adapter;

import android.content.Context;

import com.longluo.ebookreader.model.bean.SortBookBean;
import com.longluo.ebookreader.ui.adapter.view.BookSortListHolder;
import com.longluo.ebookreader.ui.base.adapter.IViewHolder;
import com.longluo.ebookreader.widget.adapter.WholeAdapter;


public class BookSortListAdapter extends WholeAdapter<SortBookBean> {
    public BookSortListAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected IViewHolder<SortBookBean> createViewHolder(int viewType) {
        return new BookSortListHolder();
    }
}
