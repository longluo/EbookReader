package com.longluo.ebookreader.ui.activity;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.app.AppActivity;
import com.longluo.ebookreader.db.BookMeta;

public class BookDetailActivity extends AppActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.book_detail_activity;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }


    private void openChapterList() {

    }

    private void readBook(BookMeta book) {
        startReadActivity(book);
    }

    private void startReadActivity(BookMeta book) {

    }

}
