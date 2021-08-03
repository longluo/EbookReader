package com.longluo.android.util;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewUtil {
    public static View findView(View container, int id) {
        View view = (View) container.getTag(id);
        if (view == null) {
            view = container.findViewById(id);
            container.setTag(id, view);
        }
        return view;
    }

    public static TextView findTextView(View container, int id) {
        return (TextView) findView(container, id);
    }

    public static ImageView findImageView(View container, int id) {
        return (ImageView) findView(container, id);
    }

    public static void setSubviewText(View view, int resourceId, String text) {
        findTextView(view, resourceId).setText(text);
    }
}
