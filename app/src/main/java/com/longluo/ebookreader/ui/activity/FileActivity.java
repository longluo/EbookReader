package com.longluo.ebookreader.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.ui.adapter.FileAdapter;
import com.longluo.ebookreader.base.BaseActivity;
import com.longluo.ebookreader.db.BookMeta;
import com.longluo.ebookreader.util.FileUtils;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class FileActivity extends BaseActivity {
    private static final String LOG_TAG = FileActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btn_choose_all)
    Button btnChooseAll;

    @BindView(R.id.btn_delete)
    Button btnDelete;

    @BindView(R.id.btn_add_file)
    Button btnAddFile;

    @BindView(R.id.rv_file_drawer)
    RecyclerView rvFileDrawer;

    public static final int EXTERNAL_STORAGE_REQ_CODE = 10;

    // 文件根目录
    private File root;
    private List<File> listFile = new ArrayList<>();
    private FileAdapter mFileAdapter;
    private SearchTextFileTask mSearchTextFileTask;
    private SaveBookToSqlLiteTask mSaveBookToSqlLiteTask;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_file;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(null);
        setSupportActionBar(toolbar);
        //设置导航图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.action_select_file);
        }

        mFileAdapter = new FileAdapter(this, listFile);
        rvFileDrawer.setLayoutManager(new LinearLayoutManager(this));
        rvFileDrawer.setAdapter(mFileAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission(FileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, EXTERNAL_STORAGE_REQ_CODE, "添加图书需要此权限，请允许");
        } else {
            root = Environment.getExternalStorageDirectory();
            searchFile();
        }
    }

    @Override
    protected void initListener() {
        mFileAdapter.setOnItemClickListener(new FileAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        });

        mFileAdapter.setOnItemLongClickListener(new FileAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {

            }
        });

        mFileAdapter.setCheckedChangeListener(new FileAdapter.CheckedChangeListener() {

            @Override
            public void onCheckedChanged(int position, CompoundButton buttonView, boolean isChecked) {
                setAddFileText(mFileAdapter.getCheckNum());
            }
        });

        //全选
        btnChooseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFileAdapter.checkAll();
            }
        });

        //取消选择
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFileAdapter.cancel();
            }
        });

        //把已经选择的书加入书架
        btnAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBookList();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearchTextFileTask != null) {
            mSearchTextFileTask.cancel(true);
        }
        if (mSaveBookToSqlLiteTask != null) {
            mSaveBookToSqlLiteTask.cancel(true);
        }
    }

    //保存选择的txt文件
    private void saveBookList() {
        List<File> files = mFileAdapter.getCheckFiles();
        if (files.size() > 0) {
            List<BookMeta> bookMetas = new ArrayList<BookMeta>();
            for (File file : files) {
                BookMeta bookMeta = new BookMeta();
                String bookName = FileUtils.getFileNameNoEx(file.getName());
                bookMeta.setBookName(bookName);
                bookMeta.setBookPath(file.getAbsolutePath());
                bookMetas.add(bookMeta);
            }
            mSaveBookToSqlLiteTask = new SaveBookToSqlLiteTask();
            mSaveBookToSqlLiteTask.execute(bookMetas);
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
                    msg = "添加书本成功";
                    setAddFileText(0);
                    mFileAdapter.cancel();
                    break;

                case REPEAT:
                    msg = "书本" + repeatBookMeta.getBookName() + "重复了";
                    break;

                default:
                    break;
            }

            Toast.makeText(FileActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    //设置添加按钮text
    protected void setAddFileText(final int num) {
        btnAddFile.post(new Runnable() {
            @Override
            public void run() {
                btnAddFile.setText("加入书架(" + num + ")项");
            }
        });
    }

    protected void searchFile() {
//        startTime = System.currentTimeMillis();
        mSearchTextFileTask = new SearchTextFileTask();
        mSearchTextFileTask.execute();
    }

    private class SearchTextFileTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            showProgress(true, "正在扫描txt文件");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            listFile = FileUtils.getSuffixFile(root.getAbsolutePath(), ".txt");
            listFile.addAll(FileUtils.getSuffixFile(root.getAbsolutePath(), ".epub"));
            listFile.addAll(FileUtils.getSuffixFile(root.getAbsolutePath(), ".azw3"));
            listFile.addAll(FileUtils.getSuffixFile(root.getAbsolutePath(), ".mobi"));

            if (listFile == null || listFile.isEmpty()) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            hideProgress();
            if (result) {
                mFileAdapter.setFiles(listFile);  //list值传到adapter
                setAddFileText(0);
            } else {
                Toast.makeText(FileActivity.this, "本机查不到txt文件", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
