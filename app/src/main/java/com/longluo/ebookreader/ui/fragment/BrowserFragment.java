package com.longluo.ebookreader.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.action.StatusAction;
import com.longluo.ebookreader.aop.CheckNet;
import com.longluo.ebookreader.aop.Log;
import com.longluo.ebookreader.app.TitleBarFragment;
import com.longluo.ebookreader.ui.activity.BrowserActivity;
import com.longluo.ebookreader.ui.activity.HomeActivity;
import com.longluo.ebookreader.widget.BrowserView;
import com.longluo.ebookreader.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

/**
 * 浏览器 Fragment
 */
public final class BrowserFragment extends TitleBarFragment<HomeActivity>
        implements StatusAction, OnRefreshListener {

    private static final String INTENT_KEY_IN_URL = "url";

    private StatusLayout mStatusLayout;
    private ProgressBar mProgressBar;
    private SmartRefreshLayout mRefreshLayout;
    private BrowserView mBrowserView;

    @CheckNet
    @Log
    public static void start(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.putExtra(INTENT_KEY_IN_URL, url);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Log
    public static BrowserFragment newInstance(String url) {
        BrowserFragment fragment = new BrowserFragment();
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_KEY_IN_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_browser;
    }

    @Override
    protected void initView() {
        mStatusLayout = findViewById(R.id.hl_browser_hint);
        mProgressBar = findViewById(R.id.pb_browser_progress);
        mRefreshLayout = findViewById(R.id.sl_browser_refresh);
        mBrowserView = findViewById(R.id.wv_browser_view);

        // 设置 WebView 生命周期回调
        mBrowserView.setLifecycleOwner(this);
        // 设置网页刷新监听
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        showLoading();

        mBrowserView.setBrowserViewClient(new AppBrowserViewClient());
        mBrowserView.setBrowserChromeClient(new AppBrowserChromeClient(mBrowserView));
        mBrowserView.loadUrl(getString(INTENT_KEY_IN_URL));
    }

    @Override
    public StatusLayout getStatusLayout() {
        return mStatusLayout;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mBrowserView.canGoBack()) {
            // 后退网页并且拦截该事件
            mBrowserView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 重新加载当前页
     */
    @CheckNet
    private void reload() {
        mBrowserView.reload();
    }

    /**
     * {@link OnRefreshListener}
     */

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        reload();
    }

    private class AppBrowserViewClient extends BrowserView.BrowserViewClient {

        /**
         * 网页加载错误时回调，这个方法会在 onPageFinished 之前调用
         */
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // 这里为什么要用延迟呢？因为加载出错之后会先调用 onReceivedError 再调用 onPageFinished
            post(() -> showError(listener -> reload()));
        }

        /**
         * 开始加载网页
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        /**
         * 完成加载网页
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            mProgressBar.setVisibility(View.GONE);
            mRefreshLayout.finishRefresh();
            showComplete();
        }
    }

    private class AppBrowserChromeClient extends BrowserView.BrowserChromeClient {

        private AppBrowserChromeClient(BrowserView view) {
            super(view);
        }

        /**
         * 收到网页标题
         */
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (title == null) {
                return;
            }

            setTitle(title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            if (icon == null) {
                return;
            }

            setRightIcon(new BitmapDrawable(getResources(), icon));
        }

        /**
         * 收到加载进度变化
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mProgressBar.setProgress(newProgress);
        }
    }
}