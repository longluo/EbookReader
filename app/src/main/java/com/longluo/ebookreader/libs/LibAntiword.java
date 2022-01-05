package com.longluo.ebookreader.libs;

/**
 * added for support doc, docx files.
 * <p>
 * by longluo
 */
public class LibAntiword {

    static {
        System.loadLibrary("antiword");
    }

    public static native int convertDocToHtml(String input, String output);
}
