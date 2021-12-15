package com.longluo.ebookreader.presenter;

import com.longluo.ebookreader.RxBus;
import com.longluo.ebookreader.model.bean.BookChapterBean;
import com.longluo.ebookreader.model.bean.BookDetailBean;
import com.longluo.ebookreader.model.bean.CallBookBean;
import com.longluo.ebookreader.model.bean.DownloadTaskBean;
import com.longluo.ebookreader.model.local.BookRepository;
import com.longluo.ebookreader.model.remote.RemoteRepository;
import com.longluo.ebookreader.presenter.contract.BookShelfContract;
import com.longluo.ebookreader.ui.base.RxPresenter;
import com.longluo.ebookreader.utils.Constant;
import com.longluo.ebookreader.utils.LogUtils;
import com.longluo.ebookreader.utils.MD5Utils;
import com.longluo.ebookreader.utils.RxUtils;
import com.longluo.ebookreader.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public class BookShelfPresenter extends RxPresenter<BookShelfContract.View>
        implements BookShelfContract.Presenter {
    private static final String TAG = "BookShelfPresenter";

    @Override
    public void refreshCallBooks() {
        List<CallBookBean> CallBooks = BookRepository
                .getInstance().getCallBooks();
        mView.finishRefresh(CallBooks);
    }

    @Override
    public void createDownloadTask(CallBookBean callBookBean) {
        DownloadTaskBean task = new DownloadTaskBean();
        task.setTaskName(callBookBean.getTitle());
        task.setBookId(callBookBean.get_id());
        task.setBookChapters(callBookBean.getBookChapters());
        task.setLastChapter(callBookBean.getBookChapters().size());

        RxBus.getInstance().post(task);
    }


    @Override
    public void loadRecommendBooks(String gender) {
        Disposable disposable = RemoteRepository.getInstance()
                .getRecommendBooks(gender)
                .doOnSuccess(new Consumer<List<CallBookBean>>() {
                    @Override
                    public void accept(List<CallBookBean> CallBooks) throws Exception{
                        //更新目录
                        updateCategory(CallBooks);
                        //异步存储到数据库中
                        BookRepository.getInstance()
                                .saveCallBooksWithAsync(CallBooks);
                    }
                })
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        beans -> {
                            mView.finishRefresh(beans);
                            mView.complete();
                        },
                        (e) -> {
                            //提示没有网络
                            LogUtils.e(e);
                            mView.showErrorTip(e.toString());
                            mView.complete();
                        }
                );
        addDisposable(disposable);
    }

    //需要修改
    @Override
    public void updateCallBooks(List<CallBookBean> callBookBeans) {
        if (callBookBeans == null || callBookBeans.isEmpty()) return;
        List<CallBookBean> CallBooks = new ArrayList<>(callBookBeans);
        List<Single<BookDetailBean>> observables = new ArrayList<>(CallBooks.size());
        Iterator<CallBookBean> it = CallBooks.iterator();
        while (it.hasNext()){
            CallBookBean CallBook = it.next();
            //删除本地文件
            if (CallBook.isLocal()) {
                it.remove();
            }
            else {
                observables.add(RemoteRepository.getInstance()
                        .getBookDetail(CallBook.get_id()));
            }
        }
        //zip可能不是一个好方法。
        Single.zip(observables, new Function<Object[], List<CallBookBean>>() {
            @Override
            public List<CallBookBean> apply(Object[] objects) throws Exception {
                List<CallBookBean> newCallBooks = new ArrayList<CallBookBean>(objects.length);
                for (int i=0; i<CallBooks.size(); ++i){
                    CallBookBean oldCallBook = CallBooks.get(i);
                    CallBookBean newCallBook = ((BookDetailBean)objects[i]).getCallBookBean();
                    //如果是oldBook是update状态，或者newCallBook与oldBook章节数不同
                    if (oldCallBook.isUpdate() ||
                            !oldCallBook.getLastChapter().equals(newCallBook.getLastChapter())){
                        newCallBook.setUpdate(true);
                    }
                    else {
                        newCallBook.setUpdate(false);
                    }
                    newCallBook.setLastRead(oldCallBook.getLastRead());
                    newCallBooks.add(newCallBook);
                    //存储到数据库中
                    BookRepository.getInstance()
                            .saveCallBooks(newCallBooks);
                }
                return newCallBooks;
            }
        })
                .compose(RxUtils::toSimpleSingle)
                .subscribe(new SingleObserver<List<CallBookBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(List<CallBookBean> value) {
                        //跟原先比较
                        mView.finishUpdate();
                        mView.complete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //提示没有网络
                        mView.showErrorTip(e.toString());
                        mView.complete();
                        LogUtils.e(e);
                    }
                });
    }

    //更新每个CallBook的目录
    private void updateCategory(List<CallBookBean> callBookBeans){
        List<Single<List<BookChapterBean>>> observables = new ArrayList<>(callBookBeans.size());
        for (CallBookBean bean : callBookBeans){
            observables.add(
                    RemoteRepository.getInstance().getBookChapters(bean.get_id())
            );
        }
        Iterator<CallBookBean> it = callBookBeans.iterator();
        //执行在上一个方法中的子线程中
        Single.concat(observables)
                .subscribe(
                        chapterList -> {

                            for (BookChapterBean bean : chapterList){
                                bean.setId(MD5Utils.strToMd5By16(bean.getLink()));
                            }

                            CallBookBean bean = it.next();
                            bean.setLastRead(StringUtils.
                                    dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
                            bean.setBookChapters(chapterList);
                        }
                );
    }
}
