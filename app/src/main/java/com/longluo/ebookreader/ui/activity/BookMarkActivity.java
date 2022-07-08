package com.longluo.ebookreader.ui.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.material.appbar.AppBarLayout;
import com.longluo.ebookreader.manager.ReadSettingManager;
import com.longluo.ebookreader.R;
import com.longluo.ebookreader.ui.adapter.MyPagerAdapter;
import com.longluo.ebookreader.base.BaseActivity;
import com.longluo.ebookreader.db.BookContent;
import com.longluo.ebookreader.util.FileUtils;
import com.longluo.ebookreader.model.PageFactory;

import java.util.ArrayList;

import butterknife.BindView;


public class BookMarkActivity extends BaseActivity {
    private static final String LOG_TAG = BookMarkActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolBar;

    @BindView(R.id.appbar)
    AppBarLayout appBar;

    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabs;

    @BindView(R.id.pager)
    ViewPager pager;

    private PageFactory pageFactory;
    private ReadSettingManager readSettingManager;
    private Typeface typeface;
    private ArrayList<BookContent> catalogueList = new ArrayList<>();
    private DisplayMetrics dm;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_bookmark;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        pageFactory = PageFactory.getInstance();
        readSettingManager = ReadSettingManager.getInstance();
        dm = getResources().getDisplayMetrics();
        typeface = readSettingManager.getTypeface();

        setSupportActionBar(toolBar);
        //设置导航图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(FileUtils.getFileName(pageFactory.getBookPath()));
        }

        setTabsValue();
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), pageFactory.getBookPath()));
        tabs.setViewPager(pager);
    }

    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);//所有初始化要在setViewPager方法之前
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, dm));
        //设置Tab标题文字的字体
        tabs.setTypeface(typeface, 0);
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(getResources().getColor(R.color.colorAccent));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);

        // pagerSlidingTabStrip.setDividerPadding(18);
    }

    @Override
    protected void initListener() {

    }
}
