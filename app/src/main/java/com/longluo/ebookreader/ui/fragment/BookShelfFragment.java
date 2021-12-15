package com.longluo.ebookreader.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.RxBus;
import com.longluo.ebookreader.event.DeleteResponseEvent;
import com.longluo.ebookreader.event.DeleteTaskEvent;
import com.longluo.ebookreader.event.DownloadMessage;
import com.longluo.ebookreader.event.RecommendBookEvent;
import com.longluo.ebookreader.model.bean.CallBookBean;
import com.longluo.ebookreader.model.local.BookRepository;
import com.longluo.ebookreader.presenter.BookShelfPresenter;
import com.longluo.ebookreader.presenter.contract.BookShelfContract;
import com.longluo.ebookreader.ui.activity.ReadActivity;
import com.longluo.ebookreader.ui.adapter.CallBookAdapter;
import com.longluo.ebookreader.ui.base.BaseMVPFragment;
import com.longluo.ebookreader.utils.RxUtils;
import com.longluo.ebookreader.utils.ToastUtils;
import com.longluo.ebookreader.widget.adapter.WholeAdapter;
import com.longluo.ebookreader.widget.itemdecoration.DividerItemDecoration;
import com.longluo.ebookreader.widget.refresh.ScrollRefreshRecyclerView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


public class BookShelfFragment extends BaseMVPFragment<BookShelfContract.Presenter>
        implements BookShelfContract.View {
    private static final String TAG = "BookShelfFragment";

    @BindView(R.id.book_shelf_rv_content)
    ScrollRefreshRecyclerView mRvContent;

    private CallBookAdapter mCallBookAdapter;
    private FooterItemView mFooterItem;

    //是否是第一次进入
    private boolean isInit = true;

    @Override
    protected int getContentId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected BookShelfContract.Presenter bindPresenter() {
        return new BookShelfPresenter();
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        //添加Footer
        mCallBookAdapter = new CallBookAdapter();
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        mRvContent.setAdapter(mCallBookAdapter);
    }

    @Override
    protected void initClick() {
        super.initClick();
        //推荐书籍
        Disposable recommendDisp = RxBus.getInstance()
                .toObservable(RecommendBookEvent.class)
                .subscribe(
                        event -> {
                            mRvContent.startRefresh();
                            mPresenter.loadRecommendBooks(event.sex);
                        }
                );
        addDisposable(recommendDisp);

        Disposable donwloadDisp = RxBus.getInstance()
                .toObservable(DownloadMessage.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        event -> {
                            //使用Toast提示
                            ToastUtils.show(event.message);
                        }
                );
        addDisposable(donwloadDisp);

        //删除书籍
        Disposable deleteDisp = RxBus.getInstance()
                .toObservable(DeleteResponseEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        event -> {
                            if (event.isDelete) {
                                ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("正在删除中");
                                progressDialog.show();
                                BookRepository.getInstance().deleteCallBookInRx(event.collBook)
                                        .compose(RxUtils::toSimpleSingle)
                                        .subscribe(
                                                (Void) -> {
                                                    mCallBookAdapter.removeItem(event.collBook);
                                                    progressDialog.dismiss();
                                                }
                                        );
                            } else {
                                //弹出一个Dialog
                                AlertDialog tipDialog = new AlertDialog.Builder(getContext())
                                        .setTitle("您的任务正在加载")
                                        .setMessage("先请暂停任务再进行删除")
                                        .setPositiveButton("确定", (dialog, which) -> {
                                            dialog.dismiss();
                                        }).create();
                                tipDialog.show();
                            }
                        }
                );
        addDisposable(deleteDisp);

        mRvContent.setOnRefreshListener(
                () -> mPresenter.updateCallBooks(mCallBookAdapter.getItems())
        );

        mCallBookAdapter.setOnItemClickListener(
                (view, pos) -> {
                    // 如果是本地文件，首先判断这个文件是否存在
                    CallBookBean collBook = mCallBookAdapter.getItem(pos);
                    if (collBook.isLocal()) {
                        //id表示本地文件的路径
                        String path = collBook.getCover();
                        File file = new File(path);
                        //判断这个本地文件是否存在
                        if (file.exists() && file.length() != 0) {
                            ReadActivity.startActivity(getContext(),
                                    mCallBookAdapter.getItem(pos), true);
                        } else {
                            String tip = getContext().getString(R.string.nb_bookshelf_book_not_exist);
                            //提示(从目录中移除这个文件)
                            new AlertDialog.Builder(getContext())
                                    .setTitle(getResources().getString(R.string.nb_common_tip))
                                    .setMessage(tip)
                                    .setPositiveButton(getResources().getString(R.string.nb_common_sure),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    deleteBook(collBook);
                                                }
                                            })
                                    .setNegativeButton(getResources().getString(R.string.nb_common_cancel), null)
                                    .show();
                        }
                    } else {
                        ReadActivity.startActivity(getContext(),
                                mCallBookAdapter.getItem(pos), true);
                    }
                }
        );

        mCallBookAdapter.setOnItemLongClickListener(
                (v, pos) -> {
                    //开启Dialog,最方便的Dialog,就是AlterDialog
                    openItemDialog(mCallBookAdapter.getItem(pos));
                    return true;
                }
        );
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mRvContent.startRefresh();
    }

    private void openItemDialog(CallBookBean collBook) {
        String[] menus;
        if (collBook.isLocal()) {
            menus = getResources().getStringArray(R.array.nb_menu_local_book);
        } else {
            menus = getResources().getStringArray(R.array.nb_menu_net_book);
        }
        AlertDialog collBookDialog = new AlertDialog.Builder(getContext())
                .setTitle(collBook.getTitle())
                .setAdapter(new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_list_item_1, menus),
                        (dialog, which) -> onItemMenuClick(menus[which], collBook))
                .setNegativeButton(null, null)
                .setPositiveButton(null, null)
                .create();

        collBookDialog.show();
    }

    private void onItemMenuClick(String which, CallBookBean collBook) {
        switch (which) {
            //置顶
            case "置顶":
                break;

            //缓存
            case "缓存":
                //2. 进行判断，如果CollBean中状态为未更新。那么就创建Task，加入到Service中去。
                //3. 如果状态为finish，并且isUpdate为true，那么就根据chapter创建状态
                //4. 如果状态为finish，并且isUpdate为false。
                downloadBook(collBook);
                break;

            //删除
            case "删除":
                deleteBook(collBook);
                break;

            //批量管理
            case "批量管理":
                break;

            default:
                break;
        }
    }

    private void downloadBook(CallBookBean collBook) {
        //创建任务
        mPresenter.createDownloadTask(collBook);
    }

    /**
     * 默认删除本地文件
     *
     * @param collBook
     */
    private void deleteBook(CallBookBean collBook) {
        if (collBook.isLocal()) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.dialog_delete, null);
            CheckBox cb = (CheckBox) view.findViewById(R.id.delete_cb_select);
            new AlertDialog.Builder(getContext())
                    .setTitle("删除文件")
                    .setView(view)
                    .setPositiveButton(getResources().getString(R.string.nb_common_sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean isSelected = cb.isSelected();
                            if (isSelected) {
                                ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("正在删除中");
                                progressDialog.show();
                                //删除
                                File file = new File(collBook.getCover());
                                if (file.exists()) file.delete();
                                BookRepository.getInstance().deleteCallBook(collBook);
                                BookRepository.getInstance().deleteBookRecord(collBook.get_id());

                                //从Adapter中删除
                                mCallBookAdapter.removeItem(collBook);
                                progressDialog.dismiss();
                            } else {
                                BookRepository.getInstance().deleteCallBook(collBook);
                                BookRepository.getInstance().deleteBookRecord(collBook.get_id());
                                //从Adapter中删除
                                mCallBookAdapter.removeItem(collBook);
                            }
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.nb_common_cancel), null)
                    .show();
        } else {
            RxBus.getInstance().post(new DeleteTaskEvent(collBook));
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {
        if (mCallBookAdapter.getItemCount() > 0 && mFooterItem == null) {
            mFooterItem = new FooterItemView();
            mCallBookAdapter.addFooterView(mFooterItem);
        }

        if (mRvContent.isRefreshing()) {
            mRvContent.finishRefresh();
        }
    }

    @Override
    public void finishRefresh(List<CallBookBean> callBookBeans) {
        mCallBookAdapter.refreshItems(callBookBeans);
        //如果是初次进入，则更新书籍信息
        if (isInit) {
            isInit = false;
            mRvContent.post(
                    () -> mPresenter.updateCallBooks(mCallBookAdapter.getItems())
            );
        }
    }

    @Override
    public void finishUpdate() {
        //重新从数据库中获取数据
        mCallBookAdapter.refreshItems(BookRepository
                .getInstance().getCallBooks());
    }

    @Override
    public void showErrorTip(String error) {
        mRvContent.setTip(error);
        mRvContent.showTip();
    }

    class FooterItemView implements WholeAdapter.ItemView {
        @Override
        public View onCreateView(ViewGroup parent) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.footer_book_shelf, parent, false);
            view.setOnClickListener(
                    (v) -> {
                        //设置RxBus回调
                    }
            );
            return view;
        }

        @Override
        public void onBindView(View view) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.refreshCallBooks();
    }
}
