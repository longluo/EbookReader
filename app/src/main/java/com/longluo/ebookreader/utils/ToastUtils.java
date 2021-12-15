package com.longluo.ebookreader.utils;

import android.widget.Toast;

import com.longluo.ebookreader.ERApplication;


public class ToastUtils {

    public static void show(String msg) {
        Toast.makeText(ERApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
