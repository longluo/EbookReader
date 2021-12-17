package com.longluo.ebookreader.presenter.contract;

import com.longluo.ebookreader.model.bean.BookDetailBean;
import com.longluo.ebookreader.model.bean.CollBookBean;
import com.longluo.ebookreader.ui.base.BaseContract;


public interface BookDetailContract {
    interface View extends BaseContract.BaseView {
        void finishRefresh(BookDetailBean bean);

        void waitToBookShelf();

        void errorToBookShelf();

        void succeedToBookShelf();
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void refreshBookDetail(String bookId);

        //添加到书架上
        void addToBookShelf(CollBookBean collBook);
    }
}
