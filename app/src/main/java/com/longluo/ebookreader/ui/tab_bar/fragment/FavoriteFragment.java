package com.longluo.ebookreader.ui.tab_bar.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.longluo.ebookreader.BR;
import com.longluo.ebookreader.R;

import androidx.annotation.Nullable;
import me.goldze.mvvmhabit.base.BaseFragment;

/**
 * Created by goldze on 2018/7/18.
 */

public class FavoriteFragment extends BaseFragment{
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_favorite;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

}
