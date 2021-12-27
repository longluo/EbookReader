package com.longluo.ebookreader.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.longluo.ebookreader.App;

public class SharedPreUtils {
    private static final String SHARED_NAME = "EbookReader_pref";

    private static SharedPreUtils sInstance;
    private SharedPreferences sharedReadable;
    private SharedPreferences.Editor sharedWritable;

    private SharedPreUtils() {
        sharedReadable = App.getContext()
                .getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        sharedWritable = sharedReadable.edit();
    }

    public static SharedPreUtils getInstance() {
        if (sInstance == null) {
            synchronized (SharedPreUtils.class) {
                if (sInstance == null) {
                    sInstance = new SharedPreUtils();
                }
            }
        }

        return sInstance;
    }

    public String getString(String key) {
        return sharedReadable.getString(key, "");
    }

    public String getString(String key, String defaultStr) {
        return sharedReadable.getString(key, defaultStr);
    }

    public void putString(String key, String value) {
        sharedWritable.putString(key, value);
        sharedWritable.commit();
    }

    public int getInt(String key, int def) {
        return sharedReadable.getInt(key, def);
    }

    public void putInt(String key, int value) {
        sharedWritable.putInt(key, value);
        sharedWritable.commit();
    }

    public float getFloat(String key, float def) {
        return sharedReadable.getFloat(key, def);
    }

    public void putFloat(String key, float value) {
        sharedWritable.putFloat(key, value);
        sharedWritable.commit();
    }

    public boolean getBoolean(String key, boolean def) {
        return sharedReadable.getBoolean(key, def);
    }

    public void putBoolean(String key, boolean value) {
        sharedWritable.putBoolean(key, value);
        sharedWritable.commit();
    }
}
