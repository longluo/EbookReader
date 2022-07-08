package com.longluo.ebookreader.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.db.BookMeta;
import com.longluo.ebookreader.ui.adapter.view.BookshelfViewHolder;

import java.util.List;


public class BookshelfAdapter extends RecyclerView.Adapter<BookshelfViewHolder> {
    private Context mContext;
    private List<BookMeta> mBookList;

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

    public BookshelfAdapter(Context context, List<BookMeta> mBookList) {
        this.mContext = context;
        this.mBookList = mBookList;
    }

    @Override
    public BookshelfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_shelf_item, parent, false);
        BookshelfViewHolder holder = new BookshelfViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookshelfViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvBookName.setText(mBookList.get(position).getBookName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(position);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onClick(position);
            }

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    public Object getItem(int position) {
        return mBookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setBookLists(List<BookMeta> books) {
        this.mBookList = books;
    }
}
