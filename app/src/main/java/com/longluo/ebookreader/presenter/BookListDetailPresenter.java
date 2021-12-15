package com.longluo.ebookreader.presenter;

import com.longluo.ebookreader.model.remote.RemoteRepository;
import com.longluo.ebookreader.presenter.contract.BookListContract;
import com.longluo.ebookreader.presenter.contract.BookListDetailContract;
import com.longluo.ebookreader.ui.base.RxPresenter;
import com.longluo.ebookreader.utils.LogUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;



public class BookListDetailPresenter extends RxPresenter<BookListDetailContract.View> implements BookListDetailContract.Presenter {
    @Override
    public void refreshBookListDetail(String detailId) {
        Disposable refreshDispo = RemoteRepository.getInstance()
                .getBookListDetail(detailId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (beans)-> {
                            mView.finishRefresh(beans);
                            mView.complete();
                        }
                        ,
                        (e) ->{
                            mView.showError();
                            LogUtils.e(e);
                        }
                );
        addDisposable(refreshDispo);
    }
}
