package com.longluo.ebookreader.ui.fragment;

import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.aop.SingleClick;
import com.longluo.ebookreader.app.TitleBarFragment;
import com.longluo.ebookreader.ui.activity.HomeActivity;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * 我的 Fragment
 */
public final class MineFragment extends TitleBarFragment<HomeActivity> {

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.mine_fragment;
    }

    @Override
    protected void initView() {
        setOnClickListener(R.id.btn_mine_about, R.id.btn_mine_browser, R.id.btn_mine_crash);
    }

    @Override
    protected void initData() {

    }

    @SingleClick
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.btn_mine_crash:
                // 上报错误到 Bugly 上
                CrashReport.postCatchedException(new IllegalStateException("are you ok?"));
                // 关闭 Bugly 异常捕捉
                CrashReport.closeBugly();
                break;

            case R.id.btn_mine_browser:
                BrowserFragment webFragment = new BrowserFragment();

                break;

            case R.id.btn_mine_about:

                break;

            default:
                break;
        }
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }
}