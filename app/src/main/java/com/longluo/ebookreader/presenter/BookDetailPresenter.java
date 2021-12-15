package com.longluo.ebookreader.presenter;

import com.longluo.ebookreader.model.bean.BookChapterBean;
import com.longluo.ebookreader.model.bean.BookDetailBean;
import com.longluo.ebookreader.model.bean.CallBookBean;
import com.longluo.ebookreader.model.local.BookRepository;
import com.longluo.ebookreader.model.remote.RemoteRepository;
import com.longluo.ebookreader.presenter.contract.BookDetailContract;
import com.longluo.ebookreader.ui.base.RxPresenter;
import com.longluo.ebookreader.utils.LogUtils;
import com.longluo.ebookreader.utils.MD5Utils;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class BookDetailPresenter extends RxPresenter<BookDetailContract.View>
        implements BookDetailContract.Presenter{
    private static final String TAG = "BookDetailPresenter";
    private String bookId;

    @Override
    public void refreshBookDetail(String bookId) {
        this.bookId = bookId;
        refreshBook();
    }

    @Override
    public void addToBookShelf(CallBookBean callBookBean)  {
        Disposable disposable = RemoteRepository.getInstance()
                .getBookChapters(callBookBean.get_id())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(
                        (d) -> mView.waitToBookShelf() //等待加载
                )
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        beans -> {
                            //设置 id
                            for(BookChapterBean bean :beans){
                                bean.setId(MD5Utils.strToMd5By16(bean.getLink()));
                            }

                            //设置目录
                            callBookBean.setBookChapters(beans);
                            //存储收藏
                            BookRepository.getInstance()
                                    .saveCallBookWithAsync(callBookBean);

                            mView.succeedToBookShelf();
                        }
                        ,
                        e -> {
                            mView.errorToBookShelf();
                            LogUtils.e(e);
                        }
                );
        addDisposable(disposable);
    }

    private void refreshBook(){
        RemoteRepository
                .getInstance()
                .getBookDetail(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<BookDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(BookDetailBean value){
                        mView.finishRefresh(value);
                        mView.complete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError();
                    }
                });
    }
}
