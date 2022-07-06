package com.longluo.ebookreader.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import android.util.StateSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.hjq.permissions.Permission;
import com.longluo.ebookreader.R;
import com.longluo.ebookreader.aop.Permissions;
import com.longluo.ebookreader.app.TitleBarFragment;
import com.longluo.ebookreader.db.BookMeta;
import com.longluo.ebookreader.ui.activity.HomeActivity;
import com.longluo.ebookreader.ui.adapter.BaseFragmentAdapter;
import com.longluo.ebookreader.util.BookUtils;
import com.longluo.ebookreader.util.FileUtils;
import com.longluo.ebookreader.widget.TextDetailDocumentsCell;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class FileExplorerFragment extends TitleBarFragment<HomeActivity> {

    private File currentDir;

    private ListView listView;
    private ListAdapter listAdapter;
    private TextView emptyView;

    private CheckBox mBtnChooseAll;
    private TextView mBtnDelete;
    private TextView mBtnAddBook;

    private DocumentSelectActivityDelegate delegate;

    private static String title = "";

    private ArrayList<ListItem> items = new ArrayList<ListItem>();
    private ArrayList<ListItem> checkItems = new ArrayList<ListItem>();
    private ArrayList<HistoryEntry> history = new ArrayList<HistoryEntry>();
    private List<BookMeta> bookMetas;
    private long sizeLimit = 1024 * 1024 * 1024;

    private String[] chooseFileType = {".txt", ".epub", ".mobi", ".azw", ".azw3"};

    private class HistoryEntry {
        int scrollItem, scrollOffset;
        File dir;
        String title;
    }

    public interface DocumentSelectActivityDelegate {
        void didSelectFiles(FileExplorerFragment activity, ArrayList<String> files);

        void startDocumentSelectActivity();

        void updateToolBarName(String name);
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
        mBtnChooseAll = findViewById(R.id.file_system_cb_selected_all);
        mBtnDelete = findViewById(R.id.file_system_btn_delete);
        mBtnAddBook = findViewById(R.id.file_system_btn_add_book);

        listAdapter = new ListAdapter(getActivity());
        emptyView = findViewById(R.id.searchEmptyView);
        emptyView.setOnTouchListener((v, event) -> true);

        listView = findViewById(R.id.listView);
        listView.setEmptyView(emptyView);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {
                if (i < 0 || i >= items.size()) {
                    return;
                }
                ListItem item = items.get(i);
                File file = item.file;
                if (file == null) {
                    HistoryEntry he = history.remove(history.size() - 1);
                    title = he.title;
                    updateName(title);
                    if (he.dir != null) {
                        listFiles(he.dir);
                    } else {
                        listRoots();
                    }
                    listView.setSelectionFromTop(he.scrollItem,
                            he.scrollOffset);
                } else if (file.isDirectory()) {
                    HistoryEntry he = new HistoryEntry();
                    he.scrollItem = listView.getFirstVisiblePosition();
                    he.scrollOffset = listView.getChildAt(0).getTop();
                    he.dir = currentDir;
                    he.title = title.toString();
                    updateName(title);
                    if (!listFiles(file)) {
                        return;
                    }
                    history.add(he);
                    title = item.title;
                    updateName(title);
                    listView.setSelection(0);
                } else {
                    if (!file.canRead()) {
                        showErrorBox("没有权限！");
                        return;
                    }
                    if (sizeLimit != 0) {
                        if (file.length() > sizeLimit) {
                            showErrorBox("文件大小超出限制！");
                            return;
                        }
                    }
                    if (file.length() == 0) {
                        return;
                    }
                    if (file.toString().contains(chooseFileType[0]) ||
                            file.toString().contains(chooseFileType[1]) ||
                            file.toString().contains(chooseFileType[2]) ||
                            file.toString().contains(chooseFileType[3]) ||
                            file.toString().contains(chooseFileType[4])) {
                        if (delegate != null) {
                            ArrayList<String> files = new ArrayList<String>();
                            files.add(file.getAbsolutePath());
                            delegate.didSelectFiles(FileExplorerFragment.this, files);
                        }
                    } else {
                        showErrorBox("请选择正确的文件！");
                        return;
                    }
                }
            }
        });

        changgeCheckBookNum();
        listRoots();
    }

    @Override
    protected void initData() {
        registerReceiver();
        requestPermission();

        mBtnChooseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAll();
                changgeCheckBookNum();
                listAdapter.notifyDataSetChanged();
                changgeCheckBookNum();
            }
        });

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkItems.clear();
                listAdapter.notifyDataSetChanged();
                changgeCheckBookNum();
            }
        });

        mBtnAddBook.setOnClickListener(v -> addCheckBook());
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Permissions({Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE})
    private void requestPermission() {
        toast("获取存储权限成功");
    }

    public boolean onBackPressed_() {
        if (history.size() > 0) {
            HistoryEntry he = history.remove(history.size() - 1);
            title = he.title;
            updateName(title);
            if (he.dir != null) {
                listFiles(he.dir);
            } else {
                listRoots();
            }
            listView.setSelectionFromTop(he.scrollItem, he.scrollOffset);
            return false;
        } else {
            return true;
        }
    }

    private void updateName(String title_) {
        if (delegate != null) {
            delegate.updateToolBarName(title_);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Runnable r = new Runnable() {
                public void run() {
                    try {
                        if (currentDir == null) {
                            listRoots();
                        } else {
                            listFiles(currentDir);
                        }
                    } catch (Exception e) {
                        Timber.e("msg " + e.toString());
                    }
                }
            };
            if (Intent.ACTION_MEDIA_UNMOUNTED.equals(intent.getAction())) {
                listView.postDelayed(r, 1000);
            } else {
                r.run();
            }
        }
    };

    public void setDelegate(DocumentSelectActivityDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        filter.addAction(Intent.ACTION_MEDIA_CHECKING);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_NOFS);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        filter.addAction(Intent.ACTION_MEDIA_SHARED);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTABLE);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addDataScheme("file");
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onResume() {
        super.onResume();
        bookMetas = LitePal.findAll(BookMeta.class);
        listAdapter.notifyDataSetChanged();
    }

    private void addCheckBook() {
        if (checkItems.size() > 0) {
            List<BookMeta> bookMetas = new ArrayList<BookMeta>();
            for (ListItem item : checkItems) {
                BookMeta bookMeta = new BookMeta();
                String bookName = FileUtils.getFileName(item.thumb);
                bookMeta.setBookName(bookName);
                bookMeta.setBookPath(item.thumb);
                bookMetas.add(bookMeta);
            }
            SaveBookToSqlLiteTask mSaveBookToSqlLiteTask = new SaveBookToSqlLiteTask();
            mSaveBookToSqlLiteTask.execute(bookMetas);
        }
    }

    private void checkAll() {
        for (ListItem listItem : items) {
            if (!TextUtils.isEmpty(listItem.thumb)) {
                boolean isCheck = false;
                for (ListItem item : checkItems) {
                    if (item.thumb.equals(listItem.thumb)) {
                        isCheck = true;
                        break;
                    }
                }
                for (BookMeta list : bookMetas) {
                    if (list.getBookPath().equals(listItem.thumb)) {
                        isCheck = true;
                        break;
                    }
                }
                if (!isCheck) {
                    checkItems.add(listItem);
                }
            }
        }
    }

    private class ListItem {
        int icon;
        String title;
        String subtitle = "";
        String ext = "";
        String thumb;
        File file;
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
                    checkItems.clear();
                    bookMetas = LitePal.findAll(BookMeta.class);
                    listAdapter.notifyDataSetChanged();
                    changgeCheckBookNum();
                    break;

                case REPEAT:
                    msg = "书本" + repeatBookMeta.getBookName() + "重复了";
                    break;
            }

            Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void listRoots() {
        currentDir = null;
        items.clear();
        String extStorage = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        ListItem ext = new ListItem();

        if (Build.VERSION.SDK_INT < 9
                || Environment.isExternalStorageRemovable()) {
            ext.title = "SdCard";
        } else {
            ext.title = "InternalStorage";
        }

        ext.icon = Build.VERSION.SDK_INT < 9
                || Environment.isExternalStorageRemovable() ? R.mipmap.ic_external_storage
                : R.mipmap.ic_storage;
        ext.subtitle = getRootSubtitle(extStorage);
        ext.file = Environment.getExternalStorageDirectory();
        items.add(ext);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(
                    "/proc/mounts"));
            String line;
            HashMap<String, ArrayList<String>> aliases = new HashMap<String, ArrayList<String>>();
            ArrayList<String> result = new ArrayList<String>();
            String extDevice = null;
            while ((line = reader.readLine()) != null) {
                if ((!line.contains("/mnt") && !line.contains("/storage") && !line
                        .contains("/sdcard"))
                        || line.contains("asec")
                        || line.contains("tmpfs") || line.contains("none")) {
                    continue;
                }
                String[] info = line.split(" ");
                if (!aliases.containsKey(info[0])) {
                    aliases.put(info[0], new ArrayList<String>());
                }
                aliases.get(info[0]).add(info[1]);
                if (info[1].equals(extStorage)) {
                    extDevice = info[0];
                }
                result.add(info[1]);
            }
            reader.close();
            if (extDevice != null) {
                result.removeAll(aliases.get(extDevice));
                for (String path : result) {
                    try {
                        ListItem item = new ListItem();
                        if (path.toLowerCase().contains("sd")) {
                            ext.title = "SdCard";
                        } else {
                            ext.title = "外部存储";
                        }
                        item.icon = R.mipmap.ic_external_storage;
                        item.subtitle = getRootSubtitle(path);
                        item.file = new File(path);
                        items.add(item);
                    } catch (Exception e) {
                        Log.e("tmessages", e.toString());
                    }
                }
            }
        } catch (Exception e) {
            Log.e("tmessages", e.toString());
        }

        ListItem fs = new ListItem();
        fs.title = "/";
        fs.subtitle = "系统目录";
        fs.icon = R.mipmap.ic_directory;
        fs.file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        items.add(fs);

        listAdapter.notifyDataSetChanged();
    }

    private boolean listFiles(File dir) {
        if (!dir.canRead()) {
            if (dir.getAbsolutePath().startsWith(
                    Environment.getExternalStorageDirectory().toString())
                    || dir.getAbsolutePath().startsWith("/sdcard")
                    || dir.getAbsolutePath().startsWith("/mnt/sdcard")) {
                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)
                        && !Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED_READ_ONLY)) {
                    currentDir = dir;
                    items.clear();
                    String state = Environment.getExternalStorageState();
                    if (Environment.MEDIA_SHARED.equals(state)) {
                        emptyView.setText("UsbActive");
                    } else {
                        emptyView.setText("NotMounted");
                    }
                    clearDrawableAnimation(listView);
                    // scrolling = true;
                    listAdapter.notifyDataSetChanged();
                    return true;
                }
            }
            showErrorBox("没有权限!");
            return false;
        }

        emptyView.setText("没有文件!");
        File[] files = null;
        try {
            files = dir.listFiles();
        } catch (Exception e) {
            showErrorBox(e.getLocalizedMessage());
            return false;
        }
        if (files == null) {
            showErrorBox("未知错误!");
            return false;
        }
        currentDir = dir;
        items.clear();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                if (lhs.isDirectory() != rhs.isDirectory()) {
                    return lhs.isDirectory() ? -1 : 1;
                }
                return lhs.getName().compareToIgnoreCase(rhs.getName());
                /*
                 * long lm = lhs.lastModified(); long rm = lhs.lastModified();
                 * if (lm == rm) { return 0; } else if (lm > rm) { return -1; }
                 * else { return 1; }
                 */
            }
        });
        for (File file : files) {
            if (file.getName().startsWith(".") || (!file.isDirectory()
                    && !file.getName().endsWith(".txt")
                    && !file.getName().endsWith(".epub")
                    && !file.getName().endsWith(".mobi")
                    && !file.getName().endsWith(".azw")
                    && !file.getName().endsWith(".azw3"))) {
                continue;
            }
            ListItem item = new ListItem();
            item.title = file.getName();
            item.file = file;
            if (file.isDirectory()) {
                item.icon = R.mipmap.ic_directory;
                item.subtitle = "文件夹";
            } else {
                String fname = file.getName();
                String[] sp = fname.split("\\.");
                item.ext = sp.length > 1 ? sp[sp.length - 1] : "?";
                item.subtitle = formatFileSize(file.length());
                fname = fname.toLowerCase();
                if (BookUtils.isBookFormatSupport(fname)) {
                    item.thumb = file.getAbsolutePath();
                }
            }
            items.add(item);
        }
        ListItem item = new ListItem();
        item.title = "..";
        item.subtitle = "文件夹";
        item.icon = R.mipmap.ic_directory;
        item.file = null;
        items.add(0, item);
        clearDrawableAnimation(listView);
        // scrolling = true;
        listAdapter.notifyDataSetChanged();
        return true;
    }

    public static String formatFileSize(long size) {
        if (size < 1024) {
            return String.format("%d B", size);
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", size / 1024.0f);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", size / 1024.0f / 1024.0f);
        } else {
            return String.format("%.1f GB", size / 1024.0f / 1024.0f / 1024.0f);
        }
    }

    public static void clearDrawableAnimation(View view) {
        if (Build.VERSION.SDK_INT < 21 || view == null) {
            return;
        }
        Drawable drawable = null;
        if (view instanceof ListView) {
            drawable = ((ListView) view).getSelector();
            if (drawable != null) {
                drawable.setState(StateSet.NOTHING);
            }
        } else {
            drawable = view.getBackground();
            if (drawable != null) {
                drawable.setState(StateSet.NOTHING);
                drawable.jumpToCurrentState();
            }
        }
    }

    public void showErrorBox(String error) {
        if (getActivity() == null) {
            return;
        }
        new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.app_name))
                .setMessage(error).setPositiveButton("OK", null).show();
    }

    public void showReadBox(final String path) {
        Timber.d("showReadBox: path=" + path);
        if (getActivity() == null) {
            return;
        }
        new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.app_name))
                .setMessage(path).setPositiveButton("阅读", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BookMeta bookMeta = new BookMeta();
                String bookName = FileUtils.getFileName(path);
                String suffix = FileUtils.getSuffix(path);
                bookMeta.setBookName(bookName);
                bookMeta.setBookPath(path);
                bookMeta.setFormat(suffix);
                boolean isSave = false;
                for (BookMeta book : bookMetas) {
                    if (book.getBookPath().equals(bookMeta.getBookPath())) {
                        isSave = true;
                    }
                }

                if (!isSave) {
                    bookMeta.save();
                }

                BookUtils.openBook(getActivity(), bookMeta);
            }
        }).show();
    }

    private String getRootSubtitle(String path) {
        StatFs stat = new StatFs(path);
        long total = (long) stat.getBlockCount() * (long) stat.getBlockSize();
        long free = (long) stat.getAvailableBlocks()
                * (long) stat.getBlockSize();
        if (total == 0) {
            return "";
        }
        return "Free " + formatFileSize(free) + " of " + formatFileSize(total);
    }

    private void changgeCheckBookNum() {
        mBtnAddBook.setText("加入书架(" + checkItems.size() + ")");
    }

    private class ListAdapter extends BaseFragmentAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public int getViewTypeCount() {
            return 2;
        }

        public int getItemViewType(int pos) {
            return items.get(pos).subtitle.length() > 0 ? 0 : 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new TextDetailDocumentsCell(mContext);
            }
            TextDetailDocumentsCell textDetailCell = (TextDetailDocumentsCell) convertView;
            final ListItem item = items.get(position);
            if (item.icon != 0) {
                ((TextDetailDocumentsCell) convertView)
                        .setTextAndValueAndTypeAndThumb(item.title,
                                item.subtitle, null, null, item.icon, false);
            } else {
                String type = item.ext.toUpperCase().substring(0,
                        Math.min(item.ext.length(), 4));

                ((TextDetailDocumentsCell) convertView)
                        .setTextAndValueAndTypeAndThumb(item.title,
                                item.subtitle, type, item.thumb, 0, isStorage(item.thumb));
            }

            textDetailCell.getCheckBox().setOnCheckedChangeListener(null);
            textDetailCell.setChecked(isCheck(item.thumb));
            textDetailCell.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkItems.add(item);
                    } else {
                        removeCheckItem(item.thumb);
                    }
                    changgeCheckBookNum();
                }
            });

            return convertView;
        }

        private boolean isCheck(String path) {
            for (ListItem item : checkItems) {
                if (item.thumb.equals(path)) {
                    return true;
                }
            }
            return false;
        }

        private void removeCheckItem(String path) {
            for (ListItem item : checkItems) {
                if (item.thumb.equals(path)) {
                    checkItems.remove(item);
                    break;
                }
            }
        }

        private boolean isStorage(String path) {
            boolean isStore = false;
            for (BookMeta bookMeta : bookMetas) {
                if (bookMeta.getBookPath().equals(path)) {
                    return true;
                }
            }

            return false;
        }
    }
}
