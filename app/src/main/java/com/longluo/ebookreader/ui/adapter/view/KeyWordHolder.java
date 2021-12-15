package com.longluo.ebookreader.ui.adapter.view;

import android.widget.TextView;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.ui.base.adapter.ViewHolderImpl;



public class KeyWordHolder extends ViewHolderImpl<String>{

    private TextView mTvName;

    @Override
    public void initView() {
        mTvName = findById(R.id.keyword_tv_name);
    }

    @Override
    public void onBind(String data, int pos) {
        mTvName.setText(data);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_keyword;
    }
}
