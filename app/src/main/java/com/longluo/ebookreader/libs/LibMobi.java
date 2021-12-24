package com.longluo.ebookreader.libmobi;

/**
 *  added for support mobi, azw, azw3, azw4.
 *
 *  by longluo
 */
public class LibMobi {

    static {
        System.loadLibrary("mobi");
    }

    public static native int convertToEpub(String input, String output);

    public static native int convertDocToHtml(String input, String output);
}
