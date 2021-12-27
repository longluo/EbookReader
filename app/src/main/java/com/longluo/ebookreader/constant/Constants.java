package com.longluo.ebookreader.constant;

import com.longluo.ebookreader.util.FileUtils;

import java.io.File;

public class Constants {

    public static final String SHARED_CONVERT_TYPE = "convert_type";

    //book type
    public static final String BOOK_TYPE_COMMENT = "normal";
    public static final String BOOK_TYPE_VOTE = "vote";

    //book state
    public static final String BOOK_STATE_NORMAL = "normal";
    public static final String BOOK_STATE_DISTILLATE = "distillate";

    //Book Date Convert Format
    public static final String FORMAT_BOOK_DATE = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_TIME = "HH:mm";
    public static final String FORMAT_FILE_DATE = "yyyy-MM-dd";

    // RxBus
    public static final int MSG_SELECTOR = 1;
    //BookCachePath (因为getCachePath引用了Context，所以必须是静态变量，不能够是静态常量)
    public static String BOOK_CACHE_PATH = FileUtils.getCachePath() + File.separator
            + "book_cache" + File.separator;
    //文件阅读记录保存的路径
    public static String BOOK_RECORD_PATH = FileUtils.getCachePath() + File.separator
            + "book_record" + File.separator;

    public final static int READ_BG_DEFAULT = 0;
    public final static int READ_BG_1 = 1;
    public final static int READ_BG_2 = 2;
    public final static int READ_BG_3 = 3;
    public final static int READ_BG_4 = 4;

    public final static int PAGE_MODE_SIMULATION = 0;
    public final static int PAGE_MODE_COVER = 1;
    public final static int PAGE_MODE_SLIDE = 2;
    public final static int PAGE_MODE_NONE = 3;
    public final static int PAGE_MODE_SCROLL = 4;
    
    public final static String FONT_TYPE_DEFAULT = "";
    public final static String FONT_TYPE_QIHEI = "font/qihei.ttf";
    public final static String FONT_TYPE_FZXINGHEI = "font/fzxinghei.ttf";
    public final static String FONT_TYPE_FZKATONG = "font/fzkatong.ttf";
    public final static String FONT_TYPE_BYSONG = "font/bysong.ttf";
}
