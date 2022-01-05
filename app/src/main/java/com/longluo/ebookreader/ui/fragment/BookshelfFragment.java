package com.longluo.ebookreader.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.app.TitleBarFragment;
import com.longluo.ebookreader.db.BookMeta;
import com.longluo.ebookreader.manager.ReadSettingManager;
import com.longluo.ebookreader.ui.activity.HomeActivity;
import com.longluo.ebookreader.ui.adapter.ShelfAdapter;
import com.longluo.ebookreader.util.BookUtils;
import com.longluo.ebookreader.widget.animation.ContentScaleAnimation;
import com.longluo.ebookreader.widget.animation.Rotate3DAnimation;
import com.longluo.ebookreader.widget.view.DragGridView;

import org.litepal.LitePal;

import java.io.File;
import java.util.List;

public class BookshelfFragment extends TitleBarFragment<HomeActivity> {

    DragGridView mBookshelf;

    private Typeface mTypeface;

    private List<BookMeta> mBookMetas;
    private ShelfAdapter mShelfAdapter;

    //点击书本的位置
    private int itemPosition;
    private TextView itemTextView;
    //点击书本在屏幕中的x，y坐标
    private int[] location = new int[2];

    private static TextView cover;
    private static ImageView content;
    //书本打开动画缩放比例
    private float scaleTimes;
    //书本打开缩放动画
    private static ContentScaleAnimation contentAnimation;
    private static Rotate3DAnimation coverAnimation;
    //书本打开缩放动画持续时间
    public static final int ANIMATION_DURATION = 800;
    //打开书本的第一个动画是否完成
    private boolean mIsOpen = false;
    //动画加载计数器  0 默认  1一个动画执行完毕   2二个动画执行完毕
    private int animationCount = 0;

    private static Boolean isExit = false;

    private ReadSettingManager readSettingManager;

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
        readSettingManager = ReadSettingManager.getInstance();

        mTypeface = readSettingManager.getTypeface();

        mBookMetas = LitePal.findAll(BookMeta.class);
        mShelfAdapter = new ShelfAdapter(getActivity(), mBookMetas);
        mBookshelf.setAdapter(mShelfAdapter);

        mBookshelf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mBookMetas.size() > position) {
                    itemPosition = position;
                    String bookname = mBookMetas.get(itemPosition).getBookName();
                    mShelfAdapter.setItemToFirst(itemPosition);
                    final BookMeta bookMeta = mBookMetas.get(itemPosition);
                    bookMeta.setId(mBookMetas.get(0).getId());
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
                                        mBookMetas = LitePal.findAll(BookMeta.class);
                                        mShelfAdapter.setBookList(mBookMetas);
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
        mBookMetas = LitePal.findAll(BookMeta.class);
        mShelfAdapter.setBookList(mBookMetas);
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
