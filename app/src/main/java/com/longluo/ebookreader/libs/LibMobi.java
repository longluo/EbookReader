package com.longluo.ebookreader.libs;

/**
 * added for support mobi, azw, azw3, azw4.
 * <p>
 * by longluo
 */
public class LibMobi {

    static {
        System.loadLibrary("mobi");
    }

    public static native int convertToEpub(String input, String output);
}
