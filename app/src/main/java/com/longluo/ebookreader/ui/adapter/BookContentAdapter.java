package com.longluo.ebookreader.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.longluo.ebookreader.Config;
import com.longluo.ebookreader.R;
import com.longluo.ebookreader.db.BookContent;
import com.longluo.ebookreader.ui.adapter.view.BookContentViewHolder;

import java.util.List;

public class BookContentAdapter extends RecyclerView.Adapter<BookContentViewHolder> {

    private Context mContext;
    private List<BookContent> mDatas;
    private Typeface typeface;
    private Config config;
    private int currentCharter = 0;

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

    public BookContentAdapter(Context context, List<BookContent> bookContentList) {
        mContext = context;
        mDatas = bookContentList;
        config = config.getInstance();
        typeface = config.getTypeface();
    }

    @Override
    public BookContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_book_content_item, parent, false);
        BookContentViewHolder holder = new BookContentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BookContentViewHolder holder, int position) {
        holder.tvBookContent.setTypeface(typeface);

        if (currentCharter == position) {
            holder.tvBookContent.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        } else {
            holder.tvBookContent.setTextColor(mContext.getResources().getColor(R.color.read_textColor));
        }
        holder.tvBookContent.setText(mDatas.get(position).getBookContent());

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

    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setCharter(int charter) {
        currentCharter = charter;
    }
}
