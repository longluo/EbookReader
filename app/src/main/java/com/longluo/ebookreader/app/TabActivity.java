package com.longluo.ebookreader.app;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.longluo.ebookreader.R;

import java.util.List;

import io.github.longluo.base.BaseActivity;
import io.github.longluo.base.FragmentStateAdapter;

public abstract class TabActivity extends BaseActivity {

    protected TabLayout mTabLayoutIndicator;
    protected ViewPager2 mViewPager;

    protected FragmentStateAdapter mFragmentPageAdapter;

    protected List<Fragment> mFragmentList;

    private List<String> mTitleList;

    protected abstract List<Fragment> createTabFragments();

    protected abstract List<String> createTabTitles();

    /**
     * 检查输入的参数是否正确。即Fragment和title是成对的。
     */
    private void checkParamsIsRight() {
        if (mFragmentList == null || mTitleList == null) {
            throw new IllegalArgumentException("fragmentList or titleList doesn't have null");
        }

        if (mFragmentList.size() != mTitleList.size()) {
            throw new IllegalArgumentException("fragment and title size must equal");
        }
    }

    @Override
    protected void initView() {
//        mTabLayoutIndicator = findViewById(R.id.tab_tl_indicator);
//        mViewPager = findViewById(R.id.tab_vp);
    }

    @Override
    protected void initData() {
        mFragmentList = createTabFragments();
        mTitleList = createTabTitles();

        checkParamsIsRight();

        mFragmentPageAdapter = new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle());
        mViewPager.setAdapter(mFragmentPageAdapter);
        mViewPager.setOffscreenPageLimit(3);

        TabLayoutMediator mediator = new TabLayoutMediator(mTabLayoutIndicator, mViewPager, (tab, position) -> {
            tab.setText(mTitleList.get(position));
        });

        mediator.attach();
    }
}
