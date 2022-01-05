package io.github.longluo.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

/**
 * FragmentStateAdapter 封装
 */
public final class FragmentStateAdapter<F extends Fragment> extends androidx.viewpager2.adapter.FragmentStateAdapter {

    /**
     * Fragment 集合
     */
    private final List<F> mFragmentSet = new ArrayList<>();

    /**
     * Fragment 标题
     */
    private final List<CharSequence> mFragmentTitles = new ArrayList<>();

    /**
     * 当前显示的Fragment
     */
    private F mShowFragment;

    /**
     * 当前 ViewPager
     */
    private ViewPager2 mViewPager;

    /**
     * 设置成懒加载模式
     */
    private boolean mLazyMode = true;

    public FragmentStateAdapter(FragmentActivity activity) {
        this(activity.getSupportFragmentManager(), activity.getLifecycle());
    }

    public FragmentStateAdapter(Fragment fragment) {
        this(fragment.getChildFragmentManager(), fragment.getLifecycle());
    }

    public FragmentStateAdapter(FragmentManager manager, Lifecycle lifecycle) {
        super(manager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragmentSet.get(position);
    }

    public F getItem(int position) {
        return mFragmentSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mFragmentSet.size();
    }

    @Nullable
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }

    /**
     * 添加 Fragment
     */
    public void addFragment(F fragment) {
        addFragment(fragment, null);
    }

    public void addFragment(F fragment, CharSequence title) {
        mFragmentSet.add(fragment);
        mFragmentTitles.add(title);

        if (mViewPager == null) {
            return;
        }

        notifyDataSetChanged();
        if (mLazyMode) {
            mViewPager.setOffscreenPageLimit(getItemCount());
        } else {
            mViewPager.setOffscreenPageLimit(1);
        }
    }

    /**
     * 获取当前的Fragment
     */
    public F getShowFragment() {
        return mShowFragment;
    }

    /**
     * 获取某个 Fragment 的索引（没有就返回 -1）
     */
    public int getFragmentIndex(Class<? extends Fragment> clazz) {
        if (clazz == null) {
            return -1;
        }
        for (int i = 0; i < mFragmentSet.size(); i++) {
            if (clazz.getName().equals(mFragmentSet.get(i).getClass().getName())) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean containsItem(long itemId) {
        return itemId > 0 && itemId <= getItemCount();
    }

    /**
     * 设置懒加载模式
     */
    public void setLazyMode(boolean lazy) {
        mLazyMode = lazy;
        refreshLazyMode();
    }

    /**
     * 刷新加载模式
     */
    private void refreshLazyMode() {
        if (mViewPager == null) {
            return;
        }

        if (mLazyMode) {
            // 设置成懒加载模式（也就是不限制 Fragment 展示的数量）
            mViewPager.setOffscreenPageLimit(getItemCount());
        } else {
            mViewPager.setOffscreenPageLimit(1);
        }
    }
}
