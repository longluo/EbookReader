package com.longluo.ebookreader.ui.adapter;

import android.content.Context;

import com.longluo.ebookreader.model.bean.BookListDetailBean;
import com.longluo.ebookreader.ui.adapter.view.BookListInfoHolder;
import com.longluo.ebookreader.ui.base.adapter.IViewHolder;
import com.longluo.ebookreader.widget.adapter.WholeAdapter;


public class BookListDetailAdapter extends WholeAdapter<BookListDetailBean.BooksBean.BookBean> {
    public BookListDetailAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected IViewHolder<BookListDetailBean.BooksBean.BookBean> createViewHolder(int viewType) {
        return new BookListInfoHolder();
    }
}
