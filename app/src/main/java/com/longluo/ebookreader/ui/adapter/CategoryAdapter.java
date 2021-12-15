package com.longluo.ebookreader.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.longluo.ebookreader.ui.adapter.view.CategoryHolder;
import com.longluo.ebookreader.ui.base.EasyAdapter;
import com.longluo.ebookreader.ui.base.adapter.IViewHolder;
import com.longluo.ebookreader.widget.page.TxtChapter;


public class CategoryAdapter extends EasyAdapter<TxtChapter> {
    private int currentSelected = 0;

    @Override
    protected IViewHolder<TxtChapter> onCreateViewHolder(int viewType) {
        return new CategoryHolder();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        CategoryHolder holder = (CategoryHolder) view.getTag();

        if (position == currentSelected) {
            holder.setSelectedChapter();
        }

        return view;
    }

    public void setChapter(int pos) {
        currentSelected = pos;
        notifyDataSetChanged();
    }
}
