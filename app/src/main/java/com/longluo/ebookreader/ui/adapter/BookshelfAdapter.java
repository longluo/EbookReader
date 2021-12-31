package com.longluo.ebookreader.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.db.BookMeta;
import com.longluo.ebookreader.manager.ReadSettingManager;
import com.longluo.ebookreader.ui.adapter.view.BookshelfViewHolder;

import java.util.ArrayList;
import java.util.List;


public class BookshelfAdapter extends RecyclerView.Adapter<BookshelfViewHolder> {
    private Context mContext;
    private List<BookMeta> mBooks;
    private static LayoutInflater inflater = null;
    private int mHidePosition = -1;
    private Typeface typeface;
    protected List<AsyncTask<Void, Void, Boolean>> myAsyncTasks = new ArrayList<>();
    private int[] firstLocation;
    private ReadSettingManager readSettingManager;

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

    public BookshelfAdapter(Context context, List<BookMeta> bookList) {
        mContext = context;
        mBooks = bookList;
        readSettingManager = ReadSettingManager.getInstance();
        typeface = readSettingManager.getTypeface();
    }

    @NonNull
    @Override
    public BookshelfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.bookshelf_item, parent, false);
        BookshelfViewHolder holder = new BookshelfViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookshelfViewHolder holder, int position) {
        BookMeta book = (BookMeta) getItem(position);

        holder.tvBookName.setText(book.getBookName());

        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(position);
            }
        });

        holder.cardView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onClick(position);
            }

            return true;
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public Object getItem(int position) {
        return mBooks.get(position);
    }

    public void setBookList(List<BookMeta> bookList) {
        mBooks = bookList;
        notifyDataSetChanged();
    }
}
