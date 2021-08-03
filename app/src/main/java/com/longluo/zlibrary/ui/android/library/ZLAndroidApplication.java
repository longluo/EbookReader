package com.longluo.zlibrary.ui.android.library;

import android.app.Application;

import com.longluo.android.ebookreader.config.ConfigShadow;
import com.longluo.zlibrary.ui.android.image.ZLAndroidImageManager;

public abstract class ZLAndroidApplication extends Application {
    private ZLAndroidLibrary myLibrary;
    private ConfigShadow myConfig;

    @Override
    public void onCreate() {
        super.onCreate();

        // this is a workaround for strange issue on some devices:
        //    NoClassDefFoundError for android.os.AsyncTask
        try {
            Class.forName("android.os.AsyncTask");
        } catch (Throwable t) {
        }

        myConfig = new ConfigShadow(this);
        new ZLAndroidImageManager();
        myLibrary = new ZLAndroidLibrary(this);
    }

    public final ZLAndroidLibrary library() {
        return myLibrary;
    }
}
