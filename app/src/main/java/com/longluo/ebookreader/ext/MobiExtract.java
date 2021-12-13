package com.longluo.ebookreader.ext;

import android.util.Log;

import com.longluo.ebookreader.libmobi.LibMobi;

import java.io.File;
import java.io.IOException;

public class MobiExtract {
    private static final String TAG = "MobiExtract";

    public static FooterNote extract(String inputPath, final String outputDir, String hashCode) throws IOException {
        try {
            LibMobi.convertToEpub(inputPath, new File(outputDir, hashCode + "").getPath());
            File result = new File(outputDir, hashCode + hashCode + ".epub");
            return new FooterNote(result.getPath(), null);
        } catch (Exception e) {
            Log.e(TAG, "" + e);
        }

        return new FooterNote("", null);
    }

}
