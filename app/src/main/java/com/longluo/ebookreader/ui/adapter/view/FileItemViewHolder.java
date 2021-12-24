package com.longluo.ebookreader.ui.adapter.view;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.longluo.ebookreader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_file_text)
    public TextView textView;

    @BindView(R.id.tv_file_text_size)
    public TextView textSize;

    @BindView(R.id.iv_file_icon)
    public ImageView fileIcon;

    @BindView(R.id.cb_file_image)
    public CheckBox checkBox;

    @BindView(R.id.ll_file_lin)
    public LinearLayout linearLayout;

    public FileItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
