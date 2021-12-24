package com.longluo.ebookreader.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.ui.adapter.view.FileItemViewHolder;
import com.longluo.ebookreader.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileAdapter extends RecyclerView.Adapter<FileItemViewHolder> {
    private Context mContext;
    private List<File> mFiles;
    private HashMap<File, Boolean> checkMap = new HashMap<>();
    private CheckedChangeListener mCheckedChangeListener;

    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public interface OnItemLongClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public FileAdapter(Context context, List<File> files) {
        mContext = context;
        mFiles = files;
        initCheckMap();
    }

    @Override
    public FileItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_file_item, parent, false);
        FileItemViewHolder holder = new FileItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FileItemViewHolder holder, int position) {
        final File file = mFiles.get(position);

        //CheckBox状态变化监听
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkMap.put(file, isChecked);
                if (mCheckedChangeListener != null) {
                    mCheckedChangeListener.onCheckedChanged(position, buttonView, isChecked);
                }
            }
        });

        initFileData(file, holder);
        initCheckBox(file, holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    longClickListener.onClick(position);
                }

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mFiles == null) {
            return 0;
        }

        return mFiles.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void initCheckMap() {
        if (mFiles != null) {
            for (File file : mFiles) {
                checkMap.put(file, false);
            }
        }
    }

    public Object getItem(int position) {
        return mFiles.get(position);
    }

    //全选
    public void checkAll() {
        Iterator iter = checkMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            checkMap.put((File) entry.getKey(), true);
        }

        notifyDataSetChanged();
    }

    //取消
    public void cancel() {
        Iterator iter = checkMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            checkMap.put((File) entry.getKey(), false);
        }

        notifyDataSetChanged();
    }

    //选择的数目
    public int getCheckNum() {
        int num = 0;
        Iterator iter = checkMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if ((Boolean) entry.getValue()) {
                num++;
            }
        }

        return num;
    }

    public List<File> getCheckFiles() {
        List<File> files = new ArrayList<>();
        Iterator iter = checkMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if ((Boolean) entry.getValue()) {
                files.add((File) entry.getKey());
            }
        }

        return files;
    }

    public HashMap<File, Boolean> getCheckMap() {
        return checkMap;
    }

    public List<File> getFiles() {
        return mFiles;
    }

    public void setFiles(List<File> files) {
        mFiles = files;
        initCheckMap();
        notifyDataSetChanged();
    }

    private void initCheckBox(File file, FileItemViewHolder holder) {
        if (checkMap.get(file) != null) {
            holder.checkBox.setChecked(checkMap.get(file));
        }
    }

    private void initFileData(File file, FileItemViewHolder holder) {
        //设置文件名
        holder.textView.setText(file.getName());
        //文件夹和文件逻辑判断
        if (file.isDirectory()) {
            holder.fileIcon.setImageResource(R.mipmap.folder);
            holder.checkBox.setVisibility(View.INVISIBLE);
            holder.textSize.setText("项");
        } else {
            holder.fileIcon.setImageResource(R.mipmap.file_type_txt);
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.textSize.setText(FileUtils.formatFileSize(file.length()));
        }
    }

    public void setCheckedChangeListener(CheckedChangeListener checkedChangeListener) {
        mCheckedChangeListener = checkedChangeListener;
    }

    public interface CheckedChangeListener {
        void onCheckedChanged(int position, CompoundButton buttonView, boolean isChecked);
    }
}
