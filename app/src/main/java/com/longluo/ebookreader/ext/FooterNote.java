package com.longluo.ebookreader.ext;

import android.util.Log;

import java.util.Map;

public class FooterNote {
    private static final String TAG = "FooterNote";

    public String path;
    public Map<String, String> notes;

    public FooterNote(String path, Map<String, String> notes) {
        this.path = path;
        this.notes = notes;
    }

    public void debugPrint() {
        Log.d(TAG, "debugPrint" + path);
        if (notes == null) {
            Log.d(TAG, "Notes is null");
            return;
        }

        for (String key : notes.keySet()) {
            Log.d(TAG, key + " = " + notes.get(key));
        }
    }

}
