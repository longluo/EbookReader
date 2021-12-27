package com.longluo.ebookreader.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment {

    private View rootView;

    protected abstract int getLayoutRes();

    protected abstract void initData(View view);

    protected abstract void initListener();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        rootView = view;
        ButterKnife.bind(this, view);
        initData(view);
        initListener();
        return view;
    }

    public View getRootView() {
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
