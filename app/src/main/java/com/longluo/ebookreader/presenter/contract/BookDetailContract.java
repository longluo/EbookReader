package com.longluo.ebookreader.presenter.contract;

import com.longluo.ebookreader.model.bean.BookDetailBean;
import com.longluo.ebookreader.model.bean.BookListBean;
import com.longluo.ebookreader.model.bean.CallBookBean;
import com.longluo.ebookreader.ui.base.BaseContract;

import java.util.List;


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
        void addToBookShelf(CallBookBean collBook);
    }
}
