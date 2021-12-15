package com.longluo.ebookreader;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.multidex.MultiDexApplication;

import com.longluo.ebookreader.service.DownloadService;
import com.squareup.leakcanary.LeakCanary;

public class ERApplication extends MultiDexApplication {

    private static Context sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = getApplicationContext();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            startService(new Intent(getContext(), DownloadService.class));
        }

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
