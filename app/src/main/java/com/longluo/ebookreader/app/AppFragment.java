package com.longluo.ebookreader.app;

import com.hjq.http.listener.OnHttpListener;
import com.longluo.ebookreader.action.ToastAction;
import com.longluo.ebookreader.http.model.HttpData;

import io.github.longluo.base.BaseFragment;
import okhttp3.Call;

/**
 * Fragment 业务基类
 */
public abstract class AppFragment<A extends AppActivity> extends BaseFragment<A>
        implements ToastAction, OnHttpListener<Object> {

    /**
     * 当前加载对话框是否在显示中
     */
    public boolean isShowDialog() {
        A activity = getAttachActivity();
        if (activity == null) {
            return false;
        }
        return activity.isShowDialog();
    }

    /**
     * 显示加载对话框
     */
    public void showDialog() {
        A activity = getAttachActivity();
        if (activity == null) {
            return;
        }
        activity.showDialog();
    }

    /**
     * 隐藏加载对话框
     */
    public void hideDialog() {
        A activity = getAttachActivity();
        if (activity == null) {
            return;
        }
        activity.hideDialog();
    }

    /**
     * {@link OnHttpListener}
     */

    @Override
    public void onStart(Call call) {
        showDialog();
    }

    @Override
    public void onSucceed(Object result) {
        if (!(result instanceof HttpData)) {
            return;
        }
        toast(((HttpData<?>) result).getMessage());
    }

    @Override
    public void onFail(Exception e) {
        toast(e.getMessage());
    }

    @Override
    public void onEnd(Call call) {
        hideDialog();
    }
}