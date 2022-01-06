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
import com.longluo.ebookreader.widget.view.DragGridView;

import org.litepal.LitePal;

import java.io.File;
import java.util.List;

import io.github.longluo.util.ToastUtils;

public class BookshelfFragment extends TitleBarFragment<HomeActivity> {

    private RecyclerView mRvBookshelf;

    private BookshelfAdapter mBookshelfAdapter;

    private List<BookMeta> mBooks;

    public static BookshelfFragment newInstance() {
        return new BookshelfFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bookshelf_fragment;
    }

    @Override
    protected void initView() {
        mRvBookshelf = findViewById(R.id.bookShelf);

    }

    @Override
    protected void initData() {
        mBooks = LitePal.findAll(BookMeta.class);

        mBookshelfAdapter = new BookshelfAdapter(getActivity(), mBooks);
        mRvBookshelf.setAdapter(mBookshelfAdapter);
        mRvBookshelf.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRvBookshelf.addItemDecoration(new DividerItemDecoration(getActivity()));

        initListener();
    }

    private void initListener() {
        mBookshelfAdapter.setOnItemClickListener(position -> {
            final BookMeta book = mBooks.get(position);
            final String path = book.getBookPath();
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
                                mBookshelfAdapter.setBookList(mBooks);
                            }
                        }).setCancelable(true).show();
                return;
            }

            BookUtils.openBook(getActivity(), book);
        });

        mBookshelfAdapter.setOnItemLongClickListener(position -> ToastUtils.showToast(getActivity(), "Click No: = " + position));
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public void onStop() {
        DragGridView.setIsShowDeleteButton(false);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        DragGridView.setIsShowDeleteButton(false);
        super.onDestroy();
    }
}
