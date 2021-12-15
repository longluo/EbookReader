package com.longluo.ebookreader.presenter.contract;

import com.longluo.ebookreader.model.bean.BookListDetailBean;
import com.longluo.ebookreader.ui.base.BaseContract;


public interface BookListDetailContract {

    interface View extends BaseContract.BaseView {
        void finishRefresh(BookListDetailBean bean);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void refreshBookListDetail(String detailId);
    }
}
