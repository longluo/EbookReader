package com.longluo.ebookreader.presenter.contract;

import com.longluo.ebookreader.model.bean.BookChapterBean;
import com.longluo.ebookreader.ui.base.BaseContract;
import com.longluo.ebookreader.widget.page.TxtChapter;

import java.util.List;


public interface ReadContract extends BaseContract {
    interface View extends BaseContract.BaseView {
        void showCategory(List<BookChapterBean> bookChapterList);

        void finishChapter();

        void errorChapter();
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadCategory(String bookId);

        void loadChapter(String bookId, List<TxtChapter> bookChapterList);
    }
}
