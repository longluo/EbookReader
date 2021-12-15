package com.longluo.ebookreader.ui.adapter;

import android.content.Context;

import com.longluo.ebookreader.model.bean.BookListBean;
import com.longluo.ebookreader.ui.adapter.view.BookListHolder;
import com.longluo.ebookreader.ui.base.adapter.IViewHolder;
import com.longluo.ebookreader.widget.adapter.WholeAdapter;


public class BookListAdapter extends WholeAdapter<BookListBean> {
    public BookListAdapter() {
    }

    public BookListAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected IViewHolder<BookListBean> createViewHolder(int viewType) {
        return new BookListHolder();
    }
}
