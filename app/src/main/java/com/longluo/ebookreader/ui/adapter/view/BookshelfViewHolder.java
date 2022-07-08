package com.longluo.ebookreader.ui.adapter.view;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.longluo.ebookreader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookshelfViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_book_name)
    public TextView tvBookName;

    public BookshelfViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
