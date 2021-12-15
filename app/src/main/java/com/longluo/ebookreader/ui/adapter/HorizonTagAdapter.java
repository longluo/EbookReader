package com.longluo.ebookreader.ui.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.longluo.ebookreader.ui.adapter.view.HorizonTagHolder;
import com.longluo.ebookreader.ui.base.adapter.BaseListAdapter;
import com.longluo.ebookreader.ui.base.adapter.BaseViewHolder;
import com.longluo.ebookreader.ui.base.adapter.IViewHolder;


public class HorizonTagAdapter extends BaseListAdapter<String> {
    private int currentSelected = 0;

    @Override
    protected IViewHolder<String> createViewHolder(int viewType) {
        return new HorizonTagHolder();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        //配置点击事件改变状态
        IViewHolder iHolder = ((BaseViewHolder) holder).holder;
        HorizonTagHolder horizonTagHolder = (HorizonTagHolder) iHolder;
        if (position == currentSelected) {
            horizonTagHolder.setSelectedTag();
        }
    }

    /***
     * 设定当前的点击事件
     * @param pos
     */
    public void setCurrentSelected(int pos) {
        selectTag(pos);
    }

    @Override
    protected void onItemClick(View v, int pos) {
        super.onItemClick(v, pos);
        selectTag(pos);
    }

    private void selectTag(int position) {
        currentSelected = position;
        notifyDataSetChanged();
    }
}
