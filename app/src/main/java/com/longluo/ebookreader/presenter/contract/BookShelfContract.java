package com.longluo.ebookreader.presenter.contract;

import com.longluo.ebookreader.model.bean.CollBookBean;
import com.longluo.ebookreader.ui.base.BaseContract;

import java.util.List;


public interface BookShelfContract {

    interface View extends BaseContract.BaseView {
        void finishRefresh(List<CollBookBean> collBookBeans);

        void finishUpdate();

        void showErrorTip(String error);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void refreshCallBooks();

        void createDownloadTask(CollBookBean collBookBean);

        void updateCallBooks(List<CollBookBean> collBookBeans);

        void loadRecommendBooks(String gender);
    }
}
