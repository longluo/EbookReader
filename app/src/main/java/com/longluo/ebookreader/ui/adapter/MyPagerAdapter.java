package com.longluo.ebookreader.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.longluo.ebookreader.ui.fragment.BookContentsFragment;
import com.longluo.ebookreader.ui.fragment.BookMarkFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private BookContentsFragment contentsFragment;
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
                if (contentsFragment == null) {
                    contentsFragment = BookContentsFragment.newInstance(bookPath);
                }
                return contentsFragment;

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
