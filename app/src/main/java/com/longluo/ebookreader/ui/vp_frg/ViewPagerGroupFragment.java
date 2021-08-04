package com.longluo.ebookreader.ui.vp_frg;

import com.longluo.ebookreader.ui.base.fragment.BasePagerFragment;
import com.longluo.ebookreader.ui.tab_bar.fragment.ExploreFragment;
import com.longluo.ebookreader.ui.tab_bar.fragment.BookshelfFragment;
import com.longluo.ebookreader.ui.tab_bar.fragment.FavoriteFragment;
import com.longluo.ebookreader.ui.tab_bar.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * Create Author：goldze
 * Create Date：2019/01/25
 * Description：ViewPager+Fragment的实现
 */

public class ViewPagerGroupFragment extends BasePagerFragment {
    @Override
    protected List<Fragment> pagerFragment() {
        List<Fragment> list = new ArrayList<>();
        list.add(new ExploreFragment());
        list.add(new BookshelfFragment());
        list.add(new FavoriteFragment());
        list.add(new SettingFragment());
        return list;
    }

    @Override
    protected List<String> pagerTitleString() {
        List<String> list = new ArrayList<>();
        list.add("page1");
        list.add("page2");
        list.add("page3");
        list.add("page4");
        return list;
    }
}
