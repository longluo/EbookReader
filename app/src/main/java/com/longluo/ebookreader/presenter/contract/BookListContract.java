package com.longluo.ebookreader.presenter.contract;

import com.longluo.ebookreader.model.bean.BookListBean;
import com.longluo.ebookreader.model.flag.BookListType;
import com.longluo.ebookreader.ui.base.BaseContract;

import java.util.List;


public interface BookListContract {
    interface View extends BaseContract.BaseView {
        void finishRefresh(List<BookListBean> beans);

        void finishLoading(List<BookListBean> beans);

        void showLoadError();
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void refreshBookList(BookListType type, String tag, int start, int limited);

        void loadBookList(BookListType type, String tag, int start, int limited);
    }
}
