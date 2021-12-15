package com.longluo.ebookreader.ui.adapter;

import com.longluo.ebookreader.ui.adapter.view.KeyWordHolder;
import com.longluo.ebookreader.ui.base.adapter.BaseListAdapter;
import com.longluo.ebookreader.ui.base.adapter.IViewHolder;


public class KeyWordAdapter extends BaseListAdapter<String> {
    @Override
    protected IViewHolder<String> createViewHolder(int viewType) {
        return new KeyWordHolder();
    }
}
