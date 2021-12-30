package com.longluo.ebookreader.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;
import com.longluo.ebookreader.R;
import com.longluo.ebookreader.aop.Log;
import com.longluo.ebookreader.manager.ActivityManager;
import com.longluo.ebookreader.other.AppConfig;
import com.longluo.ebookreader.other.CrashHandler;
import com.longluo.ebookreader.other.DebugLoggerTree;
import com.longluo.ebookreader.other.TitleBarStyle;
import com.longluo.ebookreader.other.ToastLogInterceptor;
import com.longluo.ebookreader.other.ToastStyle;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mmkv.MMKV;

import io.github.longluo.droidlib.umeng.UmengClient;
import timber.log.Timber;

/**
 * 应用入口
 */
public final class AppApplication extends Application {

    @Log("启动耗时")
    @Override
    public void onCreate() {
        super.onCreate();
        initSdk(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // 清理所有图片内存缓存
        Glide.get(this).onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // 根据手机内存剩余情况清理图片内存缓存
        Glide.get(this).onTrimMemory(level);
    }

    /**
     * 初始化一些第三方框架
     */
    public static void initSdk(Application application) {
        // 设置标题栏初始化器
        TitleBar.setDefaultStyle(new TitleBarStyle());

        // 初始化吐司
        ToastUtils.init(application, new ToastStyle());
        // 设置调试模式
        ToastUtils.setDebugMode(AppConfig.isDebug());
        // 设置 Toast 拦截器
        ToastUtils.setInterceptor(new ToastLogInterceptor());

        // 本地异常捕捉
        CrashHandler.register(application);

        // 友盟统计、登录、分享 SDK
        UmengClient.init(application, AppConfig.isLogEnable());

        // Bugly 异常捕捉
        CrashReport.initCrashReport(application, AppConfig.getBuglyId(), AppConfig.isDebug());

        // Activity 栈管理初始化
        ActivityManager.getInstance().init(application);

        // MMKV 初始化
        MMKV.initialize(application);

        // 初始化日志打印
        if (AppConfig.isLogEnable()) {
            Timber.plant(new DebugLoggerTree());
        }

        // 注册网络状态变化监听
        ConnectivityManager connectivityManager = ContextCompat.getSystemService(application, ConnectivityManager.class);
        if (connectivityManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onLost(@NonNull Network network) {
                    Activity topActivity = ActivityManager.getInstance().getTopActivity();
                    if (!(topActivity instanceof LifecycleOwner)) {
                        return;
                    }

                    LifecycleOwner lifecycleOwner = ((LifecycleOwner) topActivity);
                    if (lifecycleOwner.getLifecycle().getCurrentState() != Lifecycle.State.RESUMED) {
                        return;
                    }

                    ToastUtils.show(R.string.common_network_error);
                }
            });
        }
    }
}