package io.github.longluo.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class DisplayUtils {
    private DisplayUtils() {
        throw new AssertionError();
    }

    public static boolean isLandscape(Context context) {
        if (context == null) {
            return false;
        }
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Calculates the size of the application's window
     * @param context
     * @return Point with the window's dimenstions
     * 
     * @deprecated please use {@link #getWindowSize(Context)}
     */
    @Deprecated
    public static Point getDisplayPixelSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }
    
    /**
     * @deprecated please use {@link #getWindowPixelWidth(Context)} instead
     */
    @Deprecated
    public static int getDisplayPixelWidth(Context context) {
        return getWindowSize(context).width();
    }

    /**
     * @deprecated please use {@link #getWindowPixelHeight(Context)} instead
     */
    @Deprecated
    public static int getDisplayPixelHeight(Context context) {
        return getWindowSize(context).height();
    }

    /**
     * Calculates the width of the application's window
     * @param context
     * @return the width of the window
     */
    public static int getWindowPixelWidth(@NonNull Context context) {
        return getWindowSize(context).width();
    }

    /**
     * Calculates the height of the application's window
     * @param context
     * @return the height of the window
     */
    public static int getWindowPixelHeight(@NonNull Context context) {
        return getWindowSize(context).height();
    }

    public static Rect getWindowSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return wm.getCurrentWindowMetrics().getBounds();
        } else {
            Display display = wm.getDefaultDisplay();
            Rect rect = new Rect();
            display.getRectSize(rect);
            return rect;
        }
    }

    /**
     * @return the width of the device's screen
     */
    public static int getDisplayPixelWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static float spToPx(Context context, float sp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        final float scale = displayMetrics.scaledDensity;
        return sp * scale;
    }

    public static int dpToPx(Context context, int dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                                             context.getResources().getDisplayMetrics());
        return (int) px;
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((px / displayMetrics.density) + 0.5);
    }

    public static boolean isXLargeTablet(Context context) {
        if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
            == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            return true;
        }
        return false;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
               == Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * returns the height of the ActionBar if one is enabled - supports both the native ActionBar
     * and ActionBarSherlock - http://stackoverflow.com/a/15476793/1673548
     */
    public static int getActionBarHeight(Context context) {
        if (context == null) {
            return 0;
        }
        TypedValue tv = new TypedValue();
        if (context.getTheme() != null
            && context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }

        // if we get this far, it's because the device doesn't support an ActionBar,
        // so return the standard ActionBar height (48dp)
        return dpToPx(context, 48);
    }

    /**
     * detect when FEATURE_ACTION_BAR_OVERLAY has been set
     */
    public static boolean hasActionBarOverlay(Window window) {
        return window.hasFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
    }
}
