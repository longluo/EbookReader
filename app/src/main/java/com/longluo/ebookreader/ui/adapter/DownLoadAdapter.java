package com.longluo.ebookreader.ui.adapter;

import com.longluo.ebookreader.model.bean.DownloadTaskBean;
import com.longluo.ebookreader.ui.adapter.view.DownloadHolder;
import com.longluo.ebookreader.ui.base.adapter.BaseListAdapter;
import com.longluo.ebookreader.ui.base.adapter.IViewHolder;


public class DownLoadAdapter extends BaseListAdapter<DownloadTaskBean> {

    @Override
    protected IViewHolder<DownloadTaskBean> createViewHolder(int viewType) {
        return new DownloadHolder();
    }
}
