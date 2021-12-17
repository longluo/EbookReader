package com.longluo.ebookreader.ui.adapter;

import com.longluo.ebookreader.model.bean.CollBookBean;
import com.longluo.ebookreader.ui.adapter.view.CallBookHolder;
import com.longluo.ebookreader.ui.base.adapter.IViewHolder;
import com.longluo.ebookreader.widget.adapter.WholeAdapter;


public class CallBookAdapter extends WholeAdapter<CollBookBean> {

    @Override
    protected IViewHolder<CollBookBean> createViewHolder(int viewType) {
        return new CallBookHolder();
    }
}
