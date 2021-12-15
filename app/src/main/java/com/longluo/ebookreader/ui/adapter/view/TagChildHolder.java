package com.longluo.ebookreader.ui.adapter.view;

import androidx.core.content.ContextCompat;
import android.widget.TextView;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.ui.base.adapter.ViewHolderImpl;



public class TagChildHolder extends ViewHolderImpl<String> {
    private TextView mTvName;
    private int mSelectTag = -1;

    @Override
    public void initView(){
        mTvName = findById(R.id.tag_child_btn_name);
    }

    @Override
    public void onBind(String value, int pos) {
        mTvName.setText(value);
        //这里要重置点击事件
        if (mSelectTag == pos){
            mTvName.setTextColor(ContextCompat.getColor(getContext(),R.color.light_red));
        }
        else {
            mTvName.setTextColor(ContextCompat.getColor(getContext(),R.color.nb_text_default));
        }
    }

    public void setTagSelected(int pos){
        mSelectTag = pos;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_tag_child;
    }
}