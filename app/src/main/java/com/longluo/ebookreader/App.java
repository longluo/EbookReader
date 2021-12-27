package com.longluo.ebookreader;

import android.app.Application;
import android.content.Context;

import com.longluo.ebookreader.util.PageFactory;
import com.squareup.leakcanary.LeakCanary;

import org.litepal.LitePal;


public class App extends Application {

    private static volatile Context sInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = getApplicationContext();

        LitePal.initialize(this);
        PageFactory.createPageFactory(this);

        // 初始化内存分析工具
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }
    }

    public static Context getContext() {
        return sInstance;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
//        LOG.d("AppState save onLowMemory");
    }
}
