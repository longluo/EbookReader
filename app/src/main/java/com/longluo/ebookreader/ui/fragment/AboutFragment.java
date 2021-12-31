package com.longluo.ebookreader.ui.fragment;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.app.AppActivity;
import com.longluo.ebookreader.app.TitleBarFragment;

public class AboutFragment extends TitleBarFragment<AppActivity> {

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.about_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
