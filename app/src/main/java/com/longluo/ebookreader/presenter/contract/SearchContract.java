package com.longluo.ebookreader.presenter.contract;

import com.longluo.ebookreader.model.bean.packages.SearchBookPackage;
import com.longluo.ebookreader.ui.base.BaseContract;

import java.util.List;


public interface SearchContract extends BaseContract {

    interface View extends BaseView {
        void finishHotWords(List<String> hotWords);

        void finishKeyWords(List<String> keyWords);

        void finishBooks(List<SearchBookPackage.BooksBean> books);

        void errorBooks();
    }

    interface Presenter extends BasePresenter<View> {
        void searchHotWord();

        //搜索提示
        void searchKeyWord(String query);

        //搜索书籍
        void searchBook(String query);
    }
}
