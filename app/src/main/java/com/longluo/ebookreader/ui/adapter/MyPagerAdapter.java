package com.longluo.ebookreader.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.longluo.ebookreader.ui.fragment.BookMarkFragment;
import com.longluo.ebookreader.ui.fragment.BookContentFragment;


public class MyPagerAdapter extends FragmentPagerAdapter {
    private BookContentFragment catalogueFragment;
    private BookMarkFragment bookMarkFragment;
    private String bookPath;
    private final String[] titles = {"目录", "书签"};

    public MyPagerAdapter(FragmentManager fm, String bookPath) {
        super(fm);
        this.bookPath = bookPath;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (catalogueFragment == null) {
                    catalogueFragment = BookContentFragment.newInstance(bookPath);
                }
                return catalogueFragment;

            case 1:
                if (bookMarkFragment == null) {
                    bookMarkFragment = BookMarkFragment.newInstance(bookPath);
                }
                return bookMarkFragment;

            default:
                break;
        }

        return null;
    }
}
