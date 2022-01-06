package com.longluo.ebookreader.ui.fragment;

import android.os.AsyncTask;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.app.TitleBarFragment;
import com.longluo.ebookreader.db.BookMeta;
import com.longluo.ebookreader.ui.activity.HomeActivity;
import com.longluo.ebookreader.ui.adapter.FileSystemAdapter;
import com.longluo.ebookreader.util.BookshelfUtils;
import com.longluo.ebookreader.util.FileUtils;
import com.longluo.ebookreader.widget.refresh.RefreshLayout;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class FileExplorerFragment extends TitleBarFragment<HomeActivity> {

    private RefreshLayout mRefreshLayout;
    private RecyclerView mRvFiles;

    private CheckBox mCbFileSystemSelectedAll;
    private TextView mFileSystemBtnAdd;
    private TextView mFileSystemBtnDelete;

    private FileSystemAdapter mAdapter;
    private boolean isCheckedAll;

    private OnFileCheckedListener mListener = new OnFileCheckedListener() {
        @Override
        public void onItemCheckedChange(boolean isChecked) {
            changeMenuStatus();
        }

        @Override
        public void onCategoryChanged() {
            //状态归零
            setCheckedAll(false);
            //改变菜单
            changeMenuStatus();
            //改变是否能够全选
            changeCheckedAllStatus();
        }
    };

    //设置文件点击监听事件
    public void setOnFileCheckedListener(OnFileCheckedListener listener) {
        mListener = listener;
    }

    //文件点击监听
    public interface OnFileCheckedListener {
        void onItemCheckedChange(boolean isChecked);

        void onCategoryChanged();
    }

    public static FileExplorerFragment newInstance() {
        return new FileExplorerFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.file_explorer_fragment;
    }

    @Override
    protected void initView() {
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRvFiles = findViewById(R.id.local_book_rv_content);
        mCbFileSystemSelectedAll = findViewById(R.id.file_system_cb_selected_all);
        mFileSystemBtnAdd = findViewById(R.id.file_system_btn_add_book);
        mFileSystemBtnDelete = findViewById(R.id.file_system_btn_delete);
    }

    @Override
    protected void initData() {
        setUpAdapter();

        mRefreshLayout.showFinish();
        initListener();
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    private void setUpAdapter() {
        mAdapter = new FileSystemAdapter();
        if (getContext() != null) {
            mRvFiles.setLayoutManager(new LinearLayoutManager(getContext()));
            mRvFiles.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
            mRvFiles.setAdapter(mAdapter);
        }
    }

    private void initListener() {
        mAdapter.setOnItemClickListener(
                (view, pos) -> {
                    //如果是已加载的文件，则点击事件无效。
                    String id = mAdapter.getItem(pos).getAbsolutePath();
                    if (BookshelfUtils.getBook(id) != null) {
                        return;
                    }

                    //点击选中
                    mAdapter.setCheckedItem(pos);

                    //反馈
                    if (mListener != null) {
                        mListener.onItemCheckedChange(mAdapter.getItemIsChecked(pos));
                    }
                }
        );

        mCbFileSystemSelectedAll.setOnClickListener(
                (view) -> {
                    //设置全选状态
                    boolean isChecked = mCbFileSystemSelectedAll.isChecked();
                    setCheckedAll(isChecked);
                    //改变菜单状态
                    changeMenuStatus();
                }
        );

        mFileSystemBtnAdd.setOnClickListener(
                (v) -> {
                    //获取选中的文件
                    List<File> files = getCheckedFiles();
                    importBooks(files);
                }
        );

        mFileSystemBtnDelete.setOnClickListener(
                (v) -> {
                    //弹出，确定删除文件吗。
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.delete_file))
                            .setMessage(getString(R.string.delete_file_confirm))
                            .setPositiveButton(getResources().getString(R.string.ok), (dialog, which) -> {
                                //删除选中的文件
                                deleteCheckedFiles();
                                //提示删除文件成功
                                toast(R.string.delete_file_success);
                            })
                            .setNegativeButton(getResources().getString(R.string.cancel), null)
                            .show();
                }
        );

        setOnFileCheckedListener(mListener);
    }

    private void importBooks(List<File> bookFiles) {
        Timber.d("importBooks");

        if (bookFiles.size() > 0) {
            List<BookMeta> bookMetas = new ArrayList<BookMeta>();
            for (File book : bookFiles) {
                String bookPath = book.getAbsolutePath();

                BookMeta bookMeta = new BookMeta();
                String bookName = FileUtils.getFileName(bookPath);
                bookMeta.setBookName(bookName);
                bookMeta.setBookPath(bookPath);
                bookMetas.add(bookMeta);
            }

            SaveBookToSqlLiteTask mSaveBookToSqlLiteTask = new SaveBookToSqlLiteTask();
            mSaveBookToSqlLiteTask.execute(bookMetas);
        }
    }

    public void setChecked(boolean checked) {
        isCheckedAll = checked;
    }

    //当前fragment是否全选
    public boolean isCheckedAll() {
        return isCheckedAll;
    }

    //设置当前列表为全选
    public void setCheckedAll(boolean checkedAll) {
        if (mAdapter == null) return;

        isCheckedAll = checkedAll;
        mAdapter.setCheckedAll(checkedAll);
    }

    //获取被选中的数量
    public int getCheckedCount() {
        if (mAdapter == null) {
            return 0;
        }

        return mAdapter.getCheckedCount();
    }

    //获取被选中的文件列表
    public List<File> getCheckedFiles() {
        return mAdapter != null ? mAdapter.getCheckedFiles() : null;
    }

    //获取文件的总数
    public int getFileCount() {
        return mAdapter != null ? mAdapter.getItemCount() : 0;
    }

    //获取可点击的文件的数量
    public int getCheckableCount() {
        if (mAdapter == null) {
            return 0;
        }

        return mAdapter.getCheckableCount();
    }

    /**
     * 删除选中的文件
     */
    public void deleteCheckedFiles() {
        //删除选中的文件
        List<File> files = getCheckedFiles();
        //删除显示的文件列表
        mAdapter.removeItems(files);
        //删除选中的文件
        for (File file : files) {
            if (file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }
    }

    /**
     * 改变底部选择栏的状态
     */
    private void changeMenuStatus() {
        //点击、删除状态的设置
        if (getCheckedCount() == 0) {
            mFileSystemBtnAdd.setText(getString(R.string.nb_file_add_shelf));
            //设置某些按钮的是否可点击
            setMenuClickable(false);

            if (mCbFileSystemSelectedAll.isChecked()) {
                setChecked(false);
                mCbFileSystemSelectedAll.setChecked(isCheckedAll());
            }
        } else {
            mFileSystemBtnAdd.setText(getString(R.string.nb_file_add_shelves, getCheckedCount()));
            setMenuClickable(true);

            //全选状态的设置

            //如果选中的全部的数据，则判断为全选
            if (getCheckedCount() == getCheckableCount()) {
                //设置为全选
                setChecked(true);
                mCbFileSystemSelectedAll.setChecked(isCheckedAll());
            }
            //如果曾今是全选则替换
            else if (isCheckedAll()) {
                setChecked(false);
                mCbFileSystemSelectedAll.setChecked(isCheckedAll());
            }
        }

        //重置全选的文字
        if (isCheckedAll()) {
            mCbFileSystemSelectedAll.setText(R.string.cancel);
        } else {
            mCbFileSystemSelectedAll.setText(getString(R.string.select_all));
        }
    }

    private void setMenuClickable(boolean isClickable) {
        //设置是否可删除
        mFileSystemBtnDelete.setEnabled(isClickable);
        mFileSystemBtnDelete.setClickable(isClickable);

        //设置是否可添加书籍
        mFileSystemBtnAdd.setEnabled(isClickable);
        mFileSystemBtnAdd.setClickable(isClickable);
    }

    /**
     * 改变全选按钮的状态
     */
    private void changeCheckedAllStatus() {
        //获取可选择的文件数量
        int count = getCheckableCount();

        //设置是否能够全选
        if (count > 0) {
            mCbFileSystemSelectedAll.setClickable(true);
            mCbFileSystemSelectedAll.setEnabled(true);
        } else {
            mCbFileSystemSelectedAll.setClickable(false);
            mCbFileSystemSelectedAll.setEnabled(false);
        }
    }

    private class SaveBookToSqlLiteTask extends AsyncTask<List<BookMeta>, Void, Integer> {
        private static final int FAIL = 0;
        private static final int SUCCESS = 1;
        private static final int REPEAT = 2;
        private BookMeta repeatBookMeta;

        @Override
        protected Integer doInBackground(List<BookMeta>... params) {
            List<BookMeta> bookMetas = params[0];
            for (BookMeta bookMeta : bookMetas) {
                List<BookMeta> books = LitePal.where("bookPath = ?", bookMeta.getBookPath()).find(BookMeta.class);
                if (books.size() > 0) {
                    repeatBookMeta = bookMeta;
                    return REPEAT;
                }
            }

            try {
                LitePal.saveAll(bookMetas);
            } catch (Exception e) {
                e.printStackTrace();
                return FAIL;
            }

            return SUCCESS;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            String msg = "";
            switch (result) {
                case FAIL:
                    msg = "由于一些原因添加书本失败";
                    break;

                case SUCCESS:
                    msg = "导入书本成功";
                    mAdapter.notifyDataSetChanged();
                    break;

                case REPEAT:
                    msg = "书本" + repeatBookMeta.getBookName() + "重复了";
                    break;

                default:
                    break;
            }

            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}
