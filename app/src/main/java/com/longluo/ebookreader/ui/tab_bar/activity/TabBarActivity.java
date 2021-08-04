package com.longluo.ebookreader.ui.tab_bar.activity;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.longluo.ebookreader.BR;
import com.longluo.ebookreader.R;
import com.longluo.ebookreader.databinding.ActivityTabBarBinding;
import com.longluo.ebookreader.ui.tab_bar.fragment.ExploreFragment;
import com.longluo.ebookreader.ui.tab_bar.fragment.BookshelfFragment;
import com.longluo.ebookreader.ui.tab_bar.fragment.FavoriteFragment;
import com.longluo.ebookreader.ui.tab_bar.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

/**
 * 底部tab按钮的例子
 * 所有例子仅做参考,理解如何使用才最重要。
 * Created by goldze on 2018/7/18.
 */
public class TabBarActivity extends BaseActivity<ActivityTabBarBinding, BaseViewModel> {
    private List<Fragment> mFragments;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_tab_bar;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        //初始化Fragment
        initFragment();
        //初始化底部Button
        initBottomTab();
    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(new BookshelfFragment());
        mFragments.add(new ExploreFragment());
        mFragments.add(new FavoriteFragment());
        mFragments.add(new SettingFragment());

        //默认选中第一个
        commitAllowingStateLoss(0);
    }

    private void initBottomTab() {
        NavigationController navigationController = binding.pagerBottomTab.material()
                .addItem(R.mipmap.yingyong, getResources().getString(R.string.bookshelf_fragment_label))
                .addItem(R.mipmap.huanzhe, getResources().getString(R.string.explore_fragment_label))
                .addItem(R.mipmap.xiaoxi_select, getResources().getString(R.string.favorite_fragment_label))
                .addItem(R.mipmap.wode_select, getResources().getString(R.string.setting_fragment_label))
                .setDefaultColor(ContextCompat.getColor(this, R.color.textColorVice))
                .build();

        //底部按钮的点击事件监听
        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.frameLayout, mFragments.get(index));
//                transaction.commitAllowingStateLoss();
                commitAllowingStateLoss(index);
            }

            @Override
            public void onRepeat(int index) {

            }
        });
    }

    private void commitAllowingStateLoss(int position) {
        hideAllFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(position + "");
        if (currentFragment != null) {
            transaction.show(currentFragment);
        } else {
            currentFragment = mFragments.get(position);
            transaction.add(R.id.frameLayout, currentFragment, position + "");
        }

        transaction.commitAllowingStateLoss();
    }

    //隐藏所有Fragment
    private void hideAllFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(i + "");
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
        }

        transaction.commitAllowingStateLoss();
    }
}
