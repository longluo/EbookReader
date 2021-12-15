package com.longluo.ebookreader.ui.adapter;

import com.longluo.ebookreader.model.bean.CallBookBean;
import com.longluo.ebookreader.ui.adapter.view.CallBookHolder;
import com.longluo.ebookreader.ui.base.adapter.IViewHolder;
import com.longluo.ebookreader.widget.adapter.WholeAdapter;


public class CallBookAdapter extends WholeAdapter<CallBookBean> {

    @Override
    protected IViewHolder<CallBookBean> createViewHolder(int viewType) {
        return new CallBookHolder();
    }
}
