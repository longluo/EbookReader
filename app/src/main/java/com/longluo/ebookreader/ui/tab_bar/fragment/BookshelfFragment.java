package com.longluo.ebookreader.ui.tab_bar.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.longluo.ebookreader.BR;
import com.longluo.ebookreader.R;

import me.goldze.mvvmhabit.base.BaseFragment;


public class BookshelfFragment extends BaseFragment {
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_bookshelf;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

}
