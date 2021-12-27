package com.longluo.ebookreader.manager;

import android.graphics.Typeface;

import com.longluo.ebookreader.App;
import com.longluo.ebookreader.constant.Constants;
import com.longluo.ebookreader.util.ScreenUtils;
import com.longluo.ebookreader.util.SharedPreUtils;
import com.longluo.ebookreader.widget.page.PageMode;
import com.longluo.ebookreader.widget.page.PageStyle;


public class ReadSettingManager {
    private static final String SHARED_READ_BRIGHTNESS = "shared_read_brightness";
    private static final String SHARED_READ_IS_BRIGHTNESS_AUTO = "shared_read_is_brightness_auto";

    private static final String SHARED_READ_TEXT_SIZE = "shared_read_text_size";
    private static final String SHARED_READ_IS_TEXT_DEFAULT = "shared_read_text_default";

    private static final String SHARED_READ_PAGE_MODE = "shared_read_page_mode";
    private final static String SHARED_READ_PAGE_STYLE = "shared_book_page_style";

    private static final String SHARED_READ_NIGHT_MODE = "shared_night_mode";

    private static final String SHARED_READ_VOLUME_TURN_PAGE = "shared_read_volume_turn_page";
    private static final String SHARED_READ_FULL_SCREEN = "shared_read_full_screen";
    public static final String SHARED_READ_CONVERT_TYPE = "shared_read_convert_type";

    private final static String SHARED_READ_FONT_TYPE = "shared_read_font_type";

    private final static String SHARED_SYSTEM_LIGHT = "system_light";

    private static volatile ReadSettingManager sInstance;
    private SharedPreUtils sharedPreUtils;

    //字体
    private Typeface typeface;

    //亮度值
    private float light = 0;

    private ReadSettingManager() {
        sharedPreUtils = SharedPreUtils.getInstance();
    }

    public static ReadSettingManager getInstance() {
        if (sInstance == null) {
            synchronized (ReadSettingManager.class) {
                if (sInstance == null) {
                    sInstance = new ReadSettingManager();
                }
            }
        }

        return sInstance;
    }

    public int getBrightness() {
        return sharedPreUtils.getInt(SHARED_READ_BRIGHTNESS, 40);
    }

    public boolean isBrightnessAuto() {
        return sharedPreUtils.getBoolean(SHARED_READ_IS_BRIGHTNESS_AUTO, false);
    }

    public void setBrightness(int progress) {
        sharedPreUtils.putInt(SHARED_READ_BRIGHTNESS, progress);
    }

    public void setAutoBrightness(boolean isAuto) {
        sharedPreUtils.putBoolean(SHARED_READ_IS_BRIGHTNESS_AUTO, isAuto);
    }

    public PageMode getPageMode() {
        int mode = sharedPreUtils.getInt(SHARED_READ_PAGE_MODE, PageMode.MODE_SIMULATION.ordinal());
        return PageMode.values()[mode];
    }

    public void setPageMode(PageMode mode) {
        sharedPreUtils.putInt(SHARED_READ_PAGE_MODE, mode.ordinal());
    }

    public PageStyle getBookPageStyle() {
        int style = sharedPreUtils.getInt(SHARED_READ_PAGE_STYLE, PageStyle.BG_0.ordinal());
        return PageStyle.values()[style];
    }

    public void setBookPageStyle(PageStyle pageStyle) {
        sharedPreUtils.putInt(SHARED_READ_PAGE_STYLE, pageStyle.ordinal());
    }

    public Typeface getTypeface() {
        if (typeface == null) {
            String typePath = sharedPreUtils.getString(SHARED_READ_FONT_TYPE, Constants.FONT_TYPE_QIHEI);
            typeface = getTypeface(typePath);
        }

        return typeface;
    }

    public String getTypefacePath() {
        String path = sharedPreUtils.getString(SHARED_READ_FONT_TYPE, Constants.FONT_TYPE_QIHEI);
        return path;
    }

    public Typeface getTypeface(String typeFacePath) {
        Typeface mTypeface;
        if (typeFacePath.equals(Constants.FONT_TYPE_DEFAULT)) {
            mTypeface = Typeface.DEFAULT;
        } else {
            mTypeface = Typeface.createFromAsset(App.getContext().getAssets(), typeFacePath);
        }

        return mTypeface;
    }

    public void setTypeface(String typefacePath) {
        typeface = getTypeface(typefacePath);
        sharedPreUtils.putString(SHARED_READ_FONT_TYPE, typefacePath);
    }

    public void setDefaultTextSize(boolean isDefault) {
        sharedPreUtils.putBoolean(SHARED_READ_IS_TEXT_DEFAULT, isDefault);
    }

    public int getTextSize() {
        return sharedPreUtils.getInt(SHARED_READ_TEXT_SIZE, ScreenUtils.spToPx(28));
    }

    public void setTextSize(int textSize) {
        sharedPreUtils.putInt(SHARED_READ_TEXT_SIZE, textSize);
    }

    public boolean isDefaultTextSize() {
        return sharedPreUtils.getBoolean(SHARED_READ_IS_TEXT_DEFAULT, false);
    }

    /**
     * 获取夜间还是白天阅读模式,true为夜晚，false为白天
     */
    public boolean isNightMode() {
        return sharedPreUtils.getBoolean(SHARED_READ_NIGHT_MODE, false);
    }

    public void setNightMode(boolean isNight) {
        sharedPreUtils.putBoolean(SHARED_READ_NIGHT_MODE, isNight);
    }

    public void setVolumeTurnPage(boolean isTurn) {
        sharedPreUtils.putBoolean(SHARED_READ_VOLUME_TURN_PAGE, isTurn);
    }

    public boolean isVolumeTurnPage() {
        return sharedPreUtils.getBoolean(SHARED_READ_VOLUME_TURN_PAGE, false);
    }

    public void setFullScreen(boolean isFullScreen) {
        sharedPreUtils.putBoolean(SHARED_READ_FULL_SCREEN, isFullScreen);
    }

    public boolean isSystemLight() {
        return sharedPreUtils.getBoolean(SHARED_SYSTEM_LIGHT, true);
    }

    public void setSystemLight(Boolean isSystemLight) {
        sharedPreUtils.putBoolean(SHARED_SYSTEM_LIGHT, isSystemLight);
    }

    public float getLight() {
        if (light == 0) {
            light = sharedPreUtils.getFloat(SHARED_SYSTEM_LIGHT, 0.1f);
        }

        return light;
    }

    /**
     * 记录配置文件中亮度值
     */
    public void setLight(float light) {
        this.light = light;
        sharedPreUtils.putFloat(SHARED_SYSTEM_LIGHT, light);
    }

    public boolean isFullScreen() {
        return sharedPreUtils.getBoolean(SHARED_READ_FULL_SCREEN, false);
    }

    public void setConvertType(int convertType) {
        sharedPreUtils.putInt(SHARED_READ_CONVERT_TYPE, convertType);
    }

    public int getConvertType() {
        return sharedPreUtils.getInt(SHARED_READ_CONVERT_TYPE, 0);
    }
}
