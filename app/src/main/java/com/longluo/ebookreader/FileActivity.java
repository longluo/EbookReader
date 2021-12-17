package com.longluo.ebookreader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.longluo.ebookreader.adapter.FileAdapter;
import com.longluo.ebookreader.base.BaseActivity;
import com.longluo.ebookreader.db.BookList;
import com.longluo.ebookreader.util.FileUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class FileActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_choose_all)
    Button btnChooseAll;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.btn_add_file)
    Button btnAddFile;
    @BindView(R.id.lv_file_drawer)
    ListView lvFileDrawer;

    public static final int EXTERNAL_STORAGE_REQ_CODE = 10;

    // 文件根目录
    private File root;
    private List<File> listFile = new ArrayList<>();
    private static FileAdapter adapter;
    private SearchTextFileTask mSearchTextFileTask;
    private SaveBookToSqlLiteTask mSaveBookToSqlLiteTask;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_file;
    }

    @Override
    protected void initData() {
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
            getSupportActionBar().setTitle("导入图书");
        }

        adapter = new FileAdapter(this, listFile);
        lvFileDrawer.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission(FileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, EXTERNAL_STORAGE_REQ_CODE, "添加图书需要此权限，请允许");
        } else {
            root = Environment.getExternalStorageDirectory();
            searchFile();
        }
    }

    @Override
    protected void initListener() {
        lvFileDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                adapter.setSelectedPosition(position);
            }
        });

        lvFileDrawer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });

        adapter.setCheckedChangeListener(new FileAdapter.CheckedChangeListener() {

            @Override
            public void onCheckedChanged(int position, CompoundButton buttonView, boolean isChecked) {
                setAddFileText(adapter.getCheckNum());
            }
        });

        //全选
        btnChooseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.checkAll();
            }
        });

        //取消选择
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.cancel();
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
        List<File> files = adapter.getCheckFiles();
        if (files.size() > 0) {
            List<BookList> bookLists = new ArrayList<BookList>();
            for (File file : files) {
                BookList bookList = new BookList();
                String bookName = FileUtils.getFileNameNoEx(file.getName());
                bookList.setBookname(bookName);
                bookList.setBookpath(file.getAbsolutePath());
                bookLists.add(bookList);
            }
            mSaveBookToSqlLiteTask = new SaveBookToSqlLiteTask();
            mSaveBookToSqlLiteTask.execute(bookLists);
        }
    }

    private class SaveBookToSqlLiteTask extends AsyncTask<List<BookList>, Void, Integer> {
        private static final int FAIL = 0;
        private static final int SUCCESS = 1;
        private static final int REPEAT = 2;
        private BookList repeatBookList;

        @Override
        protected Integer doInBackground(List<BookList>... params) {
            List<BookList> bookLists = params[0];
            for (BookList bookList : bookLists) {
                List<BookList> books = DataSupport.where("bookpath = ?", bookList.getBookpath()).find(BookList.class);
                if (books.size() > 0) {
                    repeatBookList = bookList;
                    return REPEAT;
                }
            }

            try {
                DataSupport.saveAll(bookLists);
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
                    adapter.cancel();
                    break;

                case REPEAT:
                    msg = "书本" + repeatBookList.getBookname() + "重复了";
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
                adapter.setFiles(listFile);  //list值传到adapter
                setAddFileText(0);
//                endTime = System.currentTimeMillis();
//                Log.e("time",endTime - startTime + "");
            } else {
                Toast.makeText(FileActivity.this, "本机查不到txt文件", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQ_CODE:
                // 如果请求被拒绝，那么通常grantResults数组为空
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //申请成功，进行相应操作
                    root = Environment.getExternalStorageDirectory();
                    searchFile();
                } else {
                    //申请失败，可以继续向用户解释。
                }
                break;

            default:
                break;
        }
    }
}
