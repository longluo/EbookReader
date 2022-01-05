package com.longluo.ebookreader.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.app.TitleBarFragment;
import com.longluo.ebookreader.db.BookMeta;
import com.longluo.ebookreader.ui.activity.HomeActivity;
import com.longluo.ebookreader.ui.adapter.ShelfAdapter;
import com.longluo.ebookreader.util.BookUtils;
import com.longluo.ebookreader.widget.view.DragGridView;

import org.litepal.LitePal;

import java.io.File;
import java.util.List;

public class BookshelfFragment extends TitleBarFragment<HomeActivity> {

    private DragGridView mBookshelf;

    private List<BookMeta> mBooks;
    private ShelfAdapter mShelfAdapter;

    //点击书本的位置
    private int itemPosition;

    private static Boolean isExit = false;

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
        mShelfAdapter = new ShelfAdapter(getActivity(), mBooks);
        mBookshelf.setAdapter(mShelfAdapter);

        initListener();
    }

    private void initListener() {
        mBookshelf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mBooks.size() > position) {
                    itemPosition = position;
                    mShelfAdapter.setItemToFirst(itemPosition);
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
                                        mShelfAdapter.setBookList(mBooks);
                                    }
                                }).setCancelable(true).show();
                        return;
                    }

                    BookUtils.openBook(getActivity(), bookMeta);
                }
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
        mShelfAdapter.setBookList(mBooks);
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
