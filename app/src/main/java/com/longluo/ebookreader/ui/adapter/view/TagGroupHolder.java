package com.longluo.ebookreader.ui.adapter.view;

import android.widget.TextView;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.ui.base.adapter.ViewHolderImpl;



public class TagGroupHolder extends ViewHolderImpl<String> {
    private TextView mTvGroupName;

    @Override
    public void initView() {
        mTvGroupName = findById(R.id.tag_group_name);
    }

    @Override
    public void onBind(String value, int pos) {
        mTvGroupName.setText(value);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_tag_group;
    }
}
