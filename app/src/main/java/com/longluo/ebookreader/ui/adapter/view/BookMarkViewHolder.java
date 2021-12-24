package com.longluo.ebookreader.ui.adapter.view;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.longluo.ebookreader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookMarkViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_mark_content)
    public TextView tvMarkContent;

    @BindView(R.id.tv_mark_progress)
    public TextView tvMarkProgress;

    @BindView(R.id.tv_mark_time)
    public TextView tvMarkTime;

    public BookMarkViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
