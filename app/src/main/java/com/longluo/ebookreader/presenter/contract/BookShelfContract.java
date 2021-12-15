package com.longluo.ebookreader.presenter.contract;

import com.longluo.ebookreader.model.bean.CallBookBean;
import com.longluo.ebookreader.ui.base.BaseContract;

import java.util.List;


public interface BookShelfContract {

    interface View extends BaseContract.BaseView {
        void finishRefresh(List<CallBookBean> callBookBeans);

        void finishUpdate();

        void showErrorTip(String error);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void refreshCallBooks();

        void createDownloadTask(CallBookBean callBookBean);

        void updateCallBooks(List<CallBookBean> callBookBeans);

        void loadRecommendBooks(String gender);
    }
}
