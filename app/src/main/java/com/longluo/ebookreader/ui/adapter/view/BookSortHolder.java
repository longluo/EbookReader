package com.longluo.ebookreader.ui.adapter.view;

import android.widget.TextView;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.model.bean.BookSortBean;
import com.longluo.ebookreader.ui.base.adapter.ViewHolderImpl;



public class BookSortHolder extends ViewHolderImpl<BookSortBean>{

    private TextView mTvType;
    private TextView mTvCount;

    @Override
    public void initView() {
        mTvType = findById(R.id.sort_tv_type);
        mTvCount = findById(R.id.sort_tv_count);
    }

    @Override
    public void onBind(BookSortBean value, int pos) {
        mTvType.setText(value.getName());
        mTvCount.setText(getContext().getResources().getString(R.string.nb_sort_book_count,value.getBookCount()));
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_sort;
    }
}
