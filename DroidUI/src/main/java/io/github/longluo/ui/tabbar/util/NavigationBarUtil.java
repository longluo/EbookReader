package io.github.longluo.ui.tabbar.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;

public class NavigationBarUtil {
    
    public static boolean setNavigationBarHeightZero = false;
    
    public static int getNavbarHeight(Activity context) {
        if (setNavigationBarHeightZero) return 0;
        int result = 0;
        int resourceId = 0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else
            return 0;
    }
    
    private static int getRootHeight(Activity context) {
        int diaplayHeight = 0;
        Display display = context.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(point);
            diaplayHeight = point.y;
        } else {
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
            diaplayHeight = dm.heightPixels; //得到高度```
        }
        return diaplayHeight;
    }
    
}
