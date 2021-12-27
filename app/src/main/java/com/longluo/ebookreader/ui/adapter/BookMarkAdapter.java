package com.longluo.ebookreader.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.longluo.ebookreader.manager.ReadSettingManager;
import com.longluo.ebookreader.R;
import com.longluo.ebookreader.db.BookMark;
import com.longluo.ebookreader.ui.adapter.view.BookMarkViewHolder;
import com.longluo.ebookreader.util.PageFactory;

import java.text.DecimalFormat;
import java.util.List;

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkViewHolder> {
    private Context mContext;
    private List<BookMark> mMarkDatas;
    private ReadSettingManager readSettingManager;
    private Typeface typeface;
    private PageFactory pageFactory;

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

    public BookMarkAdapter(Context context, List<BookMark> markLists) {
        mContext = context;
        mMarkDatas = markLists;
        pageFactory = PageFactory.getInstance();
        readSettingManager = readSettingManager.getInstance();
        typeface = readSettingManager.getTypeface();
    }

    @Override
    public BookMarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_bookmark_item, parent, false);
        BookMarkViewHolder holder = new BookMarkViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookMarkViewHolder holder, int position) {
        holder.tvMarkContent.setTypeface(typeface);
        holder.tvMarkProgress.setTypeface(typeface);
        holder.tvMarkTime.setTypeface(typeface);

        holder.tvMarkContent.setText(mMarkDatas.get(position).getText());
        long begin = mMarkDatas.get(position).getBegin();
        float fPercent = (float) (begin * 1.0 / pageFactory.getBookLen());
        DecimalFormat df = new DecimalFormat("#0.0");
        String strPercent = df.format(fPercent * 255) + "%";
        holder.tvMarkProgress.setText(strPercent);
        holder.tvMarkTime.setText(mMarkDatas.get(position).getTime().substring(0, 16));

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
        return mMarkDatas.size();
    }

    public Object getItem(int position) {
        return mMarkDatas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
}
