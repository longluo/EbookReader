package com.longluo.ebookreader.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.app.TitleBarFragment;
import com.longluo.ebookreader.db.BookMeta;
import com.longluo.ebookreader.ui.activity.HomeActivity;
import com.longluo.ebookreader.ui.adapter.BookshelfAdapter;
import com.longluo.ebookreader.util.BookUtils;
import com.longluo.ebookreader.widget.itemdecoration.DividerItemDecoration;

import org.litepal.LitePal;

import java.io.File;
import java.util.List;

public class BookshelfFragment extends TitleBarFragment<HomeActivity> {

    private RecyclerView mBookshelf;

    private List<BookMeta> mBooks;

    private BookshelfAdapter mBookshelfAdapter;

    //点击书本的位置
    private int itemPosition;

    public static BookshelfFragment newInstance() {
        return new BookshelfFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bookshelf_fragment;
    }

    @Override
    protected void initView() {
        mBookshelf = findViewById(R.id.bookShelf);

    }

    @Override
    protected void initData() {
        mBooks = LitePal.findAll(BookMeta.class);

        mBookshelfAdapter = new BookshelfAdapter(getActivity(), mBooks);
        mBookshelf.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mBookshelf.setAdapter(mBookshelfAdapter);
        mBookshelf.addItemDecoration(new DividerItemDecoration(getActivity()));

        mBookshelfAdapter.notifyDataSetChanged();

        initListener();
    }

    private void initListener() {
        mBookshelfAdapter.setOnItemClickListener(new BookshelfAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (mBooks.size() > position) {
                    itemPosition = position;
                    final BookMeta bookMeta = mBooks.get(itemPosition);
                    bookMeta.setId(mBooks.get(0).getId());
                    final String path = bookMeta.getBookPath();
                    File file = new File(path);
                    if (!file.exists()) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle(getActivity().getString(R.string.app_name))
                                .setMessage(path + "文件不存在,是否删除该书本？")
                                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LitePal.deleteAll(BookMeta.class, "bookPath = ?", path);
                                        mBooks = LitePal.findAll(BookMeta.class);
//                                        mShelfAdapter.setmBookList(mBooks);
                                    }
                                }).setCancelable(true).show();
                        return;
                    }

                    BookUtils.openBook(getActivity(), bookMeta);
                }
            }
        });

        mBookshelfAdapter.setOnItemLongClickListener(new BookshelfAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("是否删除书本？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                LitePal.delete(BookMark.class, bookMarkList.get(position).getId());
//                                bookMarkList.clear();
//                                bookMarkList.addAll(LitePal.where("bookPath = ?", bookPath).find(BookMark.class));
//                                bookMarkAdapter.notifyDataSetChanged();

                            }
                        }).setCancelable(true).show();
            }
        });
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBooks = LitePal.findAll(BookMeta.class);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
