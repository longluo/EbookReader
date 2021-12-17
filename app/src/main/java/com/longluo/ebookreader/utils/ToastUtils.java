package com.longluo.ebookreader.utils;

import android.widget.Toast;

import com.longluo.ebookreader.App;


public class ToastUtils {

    public static void show(String msg) {
        Toast.makeText(App.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
