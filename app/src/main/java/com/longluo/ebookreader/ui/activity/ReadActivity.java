package com.longluo.ebookreader.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.SQLException;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.google.android.material.appbar.AppBarLayout;
import com.longluo.ebookreader.manager.ReadSettingManager;
import com.longluo.ebookreader.R;
import com.longluo.ebookreader.base.BaseActivity;
import com.longluo.ebookreader.db.BookMeta;
import com.longluo.ebookreader.db.BookMark;
import com.longluo.ebookreader.ui.dialog.ReadSettingDialog;
import com.longluo.ebookreader.util.BrightnessUtils;
import com.longluo.ebookreader.model.PageFactory;
import com.longluo.ebookreader.widget.page.PageView;
import com.longluo.ebookreader.widget.page.PageMode;
import com.longluo.ebookreader.widget.page.PageStyle;

import org.litepal.LitePal;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.longluo.util.StringUtils;


public class ReadActivity extends BaseActivity implements SpeechSynthesizerListener {
    private static final String LOG_TAG = ReadActivity.class.getSimpleName();

    public static final int REQUEST_MORE_SETTING = 101;

    private final static String EXTRA_BOOK = "bookList";
    private final static int MESSAGE_CHANGEPROGRESS = 1;

    @BindView(R.id.bookpage)
    PageView bookpage;

    @BindView(R.id.tv_progress)
    TextView tv_progress;
    @BindView(R.id.rl_progress)
    RelativeLayout rl_progress;

    @BindView(R.id.read_tv_pre_chapter)
    TextView mTvPreChapter;
    @BindView(R.id.read_sb_chapter_progress)
    SeekBar mSbReadProgress;
    @BindView(R.id.read_tv_next_chapter)
    TextView mTvNextChapter;

    @BindView(R.id.read_tv_contents)
    TextView mTvBookContents;
    @BindView(R.id.read_tv_day_night_mode)
    TextView mTvDayNightMode;
    @BindView(R.id.read_tv_setting)
    TextView mTvReadSetting;

    @BindView(R.id.rl_bottom)
    RelativeLayout rl_bottom;
    @BindView(R.id.tv_stop_tts_read)
    TextView tv_stop_read;
    @BindView(R.id.rl_read_bottom)
    RelativeLayout rl_read_bottom;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;

    private ReadSettingManager readSettingManager;
    private WindowManager.LayoutParams lp;
    private BookMeta bookMeta;
    private PageFactory pageFactory;
    private int screenWidth, screenHeight;

    // popwindow????????????
    private boolean isShow = false;
    private ReadSettingDialog mSettingDialog;
    private boolean isNightMode;

    // ?????????????????????
    private SpeechSynthesizer mSpeechSynthesizer;
    private boolean isSpeaking = false;
    private boolean isOnlineSDK = true;
    private String wholePageStr = "";
    private String pageSegmentStr = "";

    private Handler mainHandler;

    // ?????????????????????????????????
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                Log.e(LOG_TAG, Intent.ACTION_BATTERY_CHANGED);
                int level = intent.getIntExtra("level", 0);
                pageFactory.updateBattery(level);
            } else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                Log.e(LOG_TAG, Intent.ACTION_TIME_TICK);
                pageFactory.updateTime();
            }
        }
    };

    @Override
    public int getLayoutRes() {
        return R.layout.activity_read;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 19) {
            bookpage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.return_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        readSettingManager = ReadSettingManager.getInstance();
        pageFactory = PageFactory.getInstance();

        IntentFilter mfilter = new IntentFilter();
        mfilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mfilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(myReceiver, mfilter);

        mSettingDialog = new ReadSettingDialog(this);

        //??????????????????
        WindowManager manage = getWindowManager();
        Display display = manage.getDefaultDisplay();
        Point displaysize = new Point();
        display.getSize(displaysize);
        screenWidth = displaysize.x;
        screenHeight = displaysize.y;

        //??????????????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //??????
        hideSystemUI();

        //??????????????????
        if (ReadSettingManager.getInstance().isBrightnessAuto()) {
            BrightnessUtils.setDefaultBrightness(this);
        } else {
            BrightnessUtils.setBrightness(this, ReadSettingManager.getInstance().getBrightness());
        }

        //??????intent?????????????????????
        Intent intent = getIntent();
        bookMeta = (BookMeta) intent.getSerializableExtra(EXTRA_BOOK);

        bookpage.setPageMode(readSettingManager.getPageMode());
        pageFactory.setPageWidget(bookpage);

        try {
            pageFactory.openBook(bookMeta);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show();
        }

        isNightMode = readSettingManager.isNightMode();
        initView();
        initBaiduTTs();
    }

    private void initView() {
        toggleNightMode();

        mainHandler = new Handler() {
            /*
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj != null) {
//                    print(msg.obj.toString());
                }
            }
        };
    }

    @Override
    protected void initListener() {
        mSbReadProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float pro;

            // ?????????????????????
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pro = (float) (progress / 10000.0);
                showProgress(pro);
            }

            // ??????????????????????????????????????????????????????????????????
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            // ??????????????????
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pageFactory.changeProgress(pro);
            }
        });

        mSettingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hideSystemUI();
            }
        });

        mSettingDialog.setSettingListener(new ReadSettingDialog.SettingListener() {
            @Override
            public void changeSystemBright(Boolean isSystem, float brightness) {
                if (!isSystem) {
                    BrightnessUtils.setBrightness(ReadActivity.this, (int)brightness);
                } else {
                    int bh = BrightnessUtils.getScreenBrightness(ReadActivity.this);
                    BrightnessUtils.setBrightness(ReadActivity.this, bh);
                }
            }

            @Override
            public void changeFontSize(int fontSize) {
                pageFactory.changeFontSize(fontSize);
            }

            @Override
            public void changeTypeFace(Typeface typeface) {
                pageFactory.changeTypeface(typeface);
            }

            @Override
            public void changeBookPageStyle(PageStyle pageStyle) {
                pageFactory.changeBookPageStyle(pageStyle);
            }

            @Override
            public void changePageMode(PageMode pageMode) {
                bookpage.setPageMode(pageMode);
            }
        });

        pageFactory.setPageEvent(new PageFactory.PageEvent() {
            @Override
            public void changeProgress(float progress) {
                Message message = new Message();
                message.what = MESSAGE_CHANGEPROGRESS;
                message.obj = progress;
                mHandler.sendMessage(message);
            }
        });

        bookpage.setTouchListener(new PageView.TouchListener() {
            @Override
            public void center() {
                if (isShow) {
                    hideReadSetting();
                } else {
                    showReadSetting();
                }
            }

            @Override
            public Boolean prePage() {
                if (isShow || isSpeaking) {
                    return false;
                }
                pageFactory.prePage();
                if (pageFactory.isfirstPage()) {
                    return false;
                }

                return true;
            }

            @Override
            public Boolean nextPage() {
                Log.e("setTouchListener", "nextPage");
                if (isShow || isSpeaking) {
                    return false;
                }
                pageFactory.nextPage();
                if (pageFactory.islastPage()) {
                    return false;
                }

                return true;
            }

            @Override
            public void cancel() {
                pageFactory.cancelPage();
            }
        });

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_CHANGEPROGRESS:
                    float progress = (float) msg.obj;
                    setSeekBarProgress(progress);
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (!isShow) {
            hideSystemUI();
        }
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.resume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pageFactory.clear();
        bookpage = null;
        unregisterReceiver(myReceiver);
        isSpeaking = false;
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.release();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShow) {
                hideReadSetting();
                return true;
            }
            if (mSettingDialog.isShowing()) {
                mSettingDialog.hide();
                return true;
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.read, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_bookmark) {
            addBookmark();
        } else if (id == R.id.action_read_book) {
            startTtsReadBook();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addBookmark() {
        if (pageFactory.getCurrentPage() != null) {
            List<BookMark> bookMarkList = LitePal.where("bookPath = ? and begin = ?", pageFactory.getBookPath(), pageFactory.getCurrentPage().getBegin() + "").find(BookMark.class);

            if (!bookMarkList.isEmpty()) {
                Toast.makeText(ReadActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
            } else {

            }
        }
    }

    private void startTtsReadBook() {
        BookMark bookMark = new BookMark();
        String word = "";
        for (String line : pageFactory.getCurrentPage().getLines()) {
            word += line;
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm ss");
            String time = sf.format(new Date());
            bookMark.setTime(time);
            bookMark.setBegin(pageFactory.getCurrentPage().getBegin());
            bookMark.setText(word);
            bookMark.setBookPath(pageFactory.getBookPath());
            bookMark.save();

            Toast.makeText(ReadActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(ReadActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ReadActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
        }

        if (mSpeechSynthesizer != null) {
            wholePageStr = pageFactory.getCurrentPage().getWholePageStr();
            int len = wholePageStr.length();
            Log.d(LOG_TAG, "len = " + len + ", str=" + wholePageStr);
            if (len < 60) {
                pageSegmentStr = wholePageStr.substring(0, len);
                wholePageStr = "";
            } else {
                pageSegmentStr = wholePageStr.substring(0, 60);
                wholePageStr = wholePageStr.substring(60);
            }
            Log.d(LOG_TAG, "After len = " + wholePageStr.length() + ", str=" + wholePageStr);
            int result = mSpeechSynthesizer.speak(pageSegmentStr);
            if (result < 0) {
                Log.e(LOG_TAG, "error result = " + result);
            } else {
                hideReadSetting();
                isSpeaking = true;
            }
        }
    }

    public static boolean openBook(Activity context, final BookMeta bookMeta) {
        if (bookMeta == null) {
            throw new NullPointerException("BookList can not be null");
        }

        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra(EXTRA_BOOK, bookMeta);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        context.startActivity(intent);
        return true;
    }

//    public BookPageWidget getPageWidget() {
//        return bookpage;
//    }

    /**
     * ??????????????????????????????
     */
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        //  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    //??????????????????
    public void showProgress(float progress) {
        if (rl_progress.getVisibility() != View.VISIBLE) {
            rl_progress.setVisibility(View.VISIBLE);
        }
        setProgress(progress);
    }

    //??????????????????
    public void hideProgress() {
        rl_progress.setVisibility(View.GONE);
    }

    private void toggleNightMode() {
        if (isNightMode) {
            mTvDayNightMode.setText(StringUtils.getString(this, R.string.mode_day));
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_read_menu_morning);
            mTvDayNightMode.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        } else {
            mTvDayNightMode.setText(StringUtils.getString(this, R.string.mode_night));
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_read_menu_night);
            mTvDayNightMode.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
    }

    private void setProgress(float progress) {
        DecimalFormat decimalFormat = new DecimalFormat("00.00");//???????????????????????????????????????????????????2???,??????0??????.
        String p = decimalFormat.format(progress * 100.0);//format ?????????????????????
        tv_progress.setText(p + "%");
    }

    public void setSeekBarProgress(float progress) {
        mSbReadProgress.setProgress((int) (progress * 10000));
    }

    private void showReadSetting() {
        isShow = true;

        rl_progress.setVisibility(View.GONE);

        if (isSpeaking) {
            Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_enter);
            rl_read_bottom.startAnimation(topAnim);
            rl_read_bottom.setVisibility(View.VISIBLE);
        } else {
            showSystemUI();

            Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_enter);
            Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_enter);
            rl_bottom.startAnimation(topAnim);
            appbar.startAnimation(topAnim);
//        ll_top.startAnimation(topAnim);
            rl_bottom.setVisibility(View.VISIBLE);
//        ll_top.setVisibility(View.VISIBLE);
            appbar.setVisibility(View.VISIBLE);
        }
    }

    private void hideReadSetting() {
        isShow = false;
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_exit);
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_exit);
        if (rl_bottom.getVisibility() == View.VISIBLE) {
            rl_bottom.startAnimation(topAnim);
        }
        if (appbar.getVisibility() == View.VISIBLE) {
            appbar.startAnimation(topAnim);
        }
        if (rl_read_bottom.getVisibility() == View.VISIBLE) {
            rl_read_bottom.startAnimation(topAnim);
        }

        rl_bottom.setVisibility(View.GONE);
        rl_read_bottom.setVisibility(View.GONE);

        appbar.setVisibility(View.GONE);
        hideSystemUI();
    }

    /**
     * ??????????????????????????????????????????UI??????????????????
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????NonBlockSyntherizer?????????
     */
    private void initBaiduTTs() {
        LoggerProxy.printable(true); // ???????????????logcat???

        // 1. ????????????
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(this);

        // 2. ??????listener
        mSpeechSynthesizer.setSpeechSynthesizerListener(this);

        // 3. ??????appId???appKey.secretKey
        int result = mSpeechSynthesizer.setAppId("25367863");
        result = mSpeechSynthesizer.setApiKey("atqtokGtwgi8GIVkYMxGClnZ", "Of6IheDaVL6547r4HL6kv5WhpwX3NGsA");

        // 5. ??????setParam ??????????????????????????????????????????
        // ??????????????????????????? 0 ???????????????????????? 1 ????????????  3 ????????????<?????????> 4 ???????????????<?????????>
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // ????????????????????????0-15 ????????? 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
        // ????????????????????????0-15 ????????? 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        // ????????????????????????0-15 ????????? 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");

        // x. ?????? ??? ??????so????????????????????????????????????????????????
        Map<String, String> params = new HashMap<>();
        // ?????????????????? mSpeechSynthesizer.setParam??????

        // 6. ?????????
        result = mSpeechSynthesizer.initTts(TtsMode.ONLINE);

    }

    @OnClick({R.id.read_tv_pre_chapter, R.id.read_tv_next_chapter, R.id.read_tv_contents,
            R.id.read_tv_day_night_mode, R.id.read_tv_setting, R.id.tv_stop_tts_read})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.read_tv_pre_chapter:
                pageFactory.preChapter();
                break;

            case R.id.read_tv_next_chapter:
                pageFactory.nextChapter();
                break;

            case R.id.read_tv_contents:
                Intent intent = new Intent(ReadActivity.this, BookMarkActivity.class);
                startActivity(intent);
                break;

            case R.id.read_tv_day_night_mode:
                isNightMode = !isNightMode;
                toggleNightMode();
                readSettingManager.setNightMode(isNightMode);
                pageFactory.setDayOrNight(isNightMode);
                break;

            case R.id.read_tv_setting:
                hideReadSetting();
                mSettingDialog.show();
                break;

            case R.id.tv_stop_tts_read:
                if (mSpeechSynthesizer != null) {
                    mSpeechSynthesizer.stop();
                    isSpeaking = false;
                    hideReadSetting();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onSynthesizeStart(String s) {
        Log.d(LOG_TAG, "onSynthesizeStart, s=" + s);

    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param utteranceId
     * @param data        ??????????????????????????????????????????????????????16K???2???????????????????????????pcm?????????
     * @param progress    ???????????????????????????????????????:????????? ?????????0-3
     */
    @Override
    public void onSynthesizeDataArrived(String utteranceId, byte[] data, int progress, int engineType) {
        Log.d(LOG_TAG, "onSynthesizeDataArrived, progress=" + progress);

    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????????????????onError????????????????????????
     *
     * @param utteranceId
     */
    @Override
    public void onSynthesizeFinish(String utteranceId) {
        Log.d(LOG_TAG, "onSynthesizeFinish, utteranceId=" + utteranceId);

    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param utteranceId
     */
    @Override
    public void onSpeechStart(String utteranceId) {
        Log.d(LOG_TAG, "onSpeechStart, utteranceId=" + utteranceId);

    }

    /**
     * ??????????????????????????????????????????
     *
     * @param utteranceId
     * @param progress    ???????????????????????????????????????:????????? ?????????0-3
     */
    @Override
    public void onSpeechProgressChanged(String utteranceId, int progress) {
        Log.d(LOG_TAG, "onSpeechProgressChanged, utteranceId=" + utteranceId);

    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????????????????onError,?????????????????????
     *
     * @param utteranceId
     */
    @Override
    public void onSpeechFinish(String utteranceId) {
        Log.d(LOG_TAG, "onSpeechFinish, utteranceId=" + utteranceId);

        if (wholePageStr.length() > 0) {
            int len = wholePageStr.length();
            Log.d(LOG_TAG, "len = " + len + ", str=" + wholePageStr);
            if (len < 60) {
                pageSegmentStr = wholePageStr.substring(0, len);
                wholePageStr = "";
            } else {
                pageSegmentStr = wholePageStr.substring(0, 60);
                wholePageStr = wholePageStr.substring(60);
            }
//            Log.d(LOG_TAG, "After len = " + wholePageStr.length() + ", str=" + wholePageStr);

            int result = mSpeechSynthesizer.speak(pageSegmentStr);
            if (result < 0) {
                Log.e(LOG_TAG, "error result = " + result);
            } else {
                hideReadSetting();
                isSpeaking = true;
            }
        } else {
            pageFactory.nextPage();
            if (pageFactory.islastPage()) {
                isSpeaking = false;
                Toast.makeText(ReadActivity.this, "?????????????????????", Toast.LENGTH_SHORT);
            } else {
                isSpeaking = true;
                pageFactory.getCurrentPage().setWholePageStr();
                wholePageStr = pageFactory.getCurrentPage().getWholePageStr();
                pageSegmentStr = wholePageStr.substring(0, 60);
                wholePageStr = wholePageStr.substring(60);
                mSpeechSynthesizer.speak(pageSegmentStr);
            }
        }
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param utteranceId
     * @param error       ??????????????????????????????
     */
    @Override
    public void onError(String utteranceId, SpeechError error) {
        Log.e(LOG_TAG, "onError, utteranceId=" + utteranceId + ", error=" + error.code + "," + error.description);

        mSpeechSynthesizer.stop();
        isSpeaking = false;
    }
}
