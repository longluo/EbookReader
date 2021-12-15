package com.longluo.ebookreader.presenter;

import com.longluo.ebookreader.model.remote.RemoteRepository;
import com.longluo.ebookreader.presenter.contract.SearchContract;
import com.longluo.ebookreader.ui.base.RxPresenter;
import com.longluo.ebookreader.utils.LogUtils;
import com.longluo.ebookreader.utils.RxUtils;

import io.reactivex.disposables.Disposable;


public class SearchPresenter extends RxPresenter<SearchContract.View>
        implements SearchContract.Presenter {

    @Override
    public void searchHotWord() {
        Disposable disp = RemoteRepository.getInstance()
                .getHotWords()
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        bean -> {
                            mView.finishHotWords(bean);
                        },
                        e -> {
                            LogUtils.e(e);
                        }
                );
        addDisposable(disp);
    }

    @Override
    public void searchKeyWord(String query) {
        Disposable disp = RemoteRepository.getInstance()
                .getKeyWords(query)
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        bean -> {
                            mView.finishKeyWords(bean);
                        },
                        e -> {
                            LogUtils.e(e);
                        }
                );
        addDisposable(disp);
    }

    @Override
    public void searchBook(String query) {
        Disposable disp = RemoteRepository.getInstance()
                .getSearchBooks(query)
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        bean -> {
                            mView.finishBooks(bean);
                        },
                        e -> {
                            LogUtils.e(e);
                            mView.errorBooks();
                        }
                );
        addDisposable(disp);
    }
}
