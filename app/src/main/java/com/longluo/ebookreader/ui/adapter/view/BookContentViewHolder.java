package com.longluo.ebookreader.ui.adapter.view;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.longluo.ebookreader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookContentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_book_content)
    public TextView tvBookContent;

    public BookContentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
