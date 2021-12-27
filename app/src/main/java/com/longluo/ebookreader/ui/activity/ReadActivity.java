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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.google.android.material.appbar.AppBarLayout;
import com.longluo.ebookreader.Config;
import com.longluo.ebookreader.R;
import com.longluo.ebookreader.base.BaseActivity;
import com.longluo.ebookreader.db.BookMeta;
import com.longluo.ebookreader.db.BookMark;
import com.longluo.ebookreader.ui.dialog.PageModeDialog;
import com.longluo.ebookreader.ui.dialog.ReadSettingDialog;
import com.longluo.ebookreader.util.BrightnessUtils;
import com.longluo.ebookreader.util.PageFactory;
import com.longluo.ebookreader.view.PageWidget;


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


public class ReadActivity extends BaseActivity implements SpeechSynthesizerListener {
    private static final String LOG_TAG = ReadActivity.class.getSimpleName();

    public static final int REQUEST_MORE_SETTING = 101;

    private final static String EXTRA_BOOK = "bookList";
    private final static int MESSAGE_CHANGEPROGRESS = 1;

    @BindView(R.id.bookpage)
    PageWidget bookpage;

    @BindView(R.id.tv_progress)
    TextView tv_progress;
    @BindView(R.id.rl_progress)
    RelativeLayout rl_progress;
    @BindView(R.id.tv_pre)
    TextView tv_pre;
    @BindView(R.id.sb_progress)
    SeekBar sb_progress;
    @BindView(R.id.tv_next)
    TextView tv_next;
    @BindView(R.id.tv_directory)
    TextView tv_directory;
    @BindView(R.id.tv_dayornight)
    TextView tv_dayornight;
    @BindView(R.id.tv_pagemode)
    TextView tv_pagemode;
    @BindView(R.id.tv_setting)
    TextView tv_setting;
    @BindView(R.id.bookpop_bottom)
    LinearLayout bookpop_bottom;
    @BindView(R.id.rl_bottom)
    RelativeLayout rl_bottom;
    @BindView(R.id.tv_stop_read)
    TextView tv_stop_read;
    @BindView(R.id.rl_read_bottom)
    RelativeLayout rl_read_bottom;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;

    private Config config;
    private WindowManager.LayoutParams lp;
    private BookMeta bookMeta;
    private PageFactory pageFactory;
    private int screenWidth, screenHeight;

    // popwindow是否显示
    private Boolean isShow = false;
    private ReadSettingDialog mSettingDialog;
    private PageModeDialog mPageModeDialog;
    private Boolean mDayOrNight;

    // 语音合成客户端
    private SpeechSynthesizer mSpeechSynthesizer;
    private boolean isSpeaking = false;
    private boolean isOnlineSDK = true;
    private String wholePageStr = "";
    private String pageSegmentStr = "";

    private Handler mainHandler;

    // 接收电池信息更新的广播
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

        config = Config.getInstance();
        pageFactory = PageFactory.getInstance();

        IntentFilter mfilter = new IntentFilter();
        mfilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mfilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(myReceiver, mfilter);

        mSettingDialog = new ReadSettingDialog(this);
        mPageModeDialog = new PageModeDialog(this);
        //获取屏幕宽高
        WindowManager manage = getWindowManager();
        Display display = manage.getDefaultDisplay();
        Point displaysize = new Point();
        display.getSize(displaysize);
        screenWidth = displaysize.x;
        screenHeight = displaysize.y;
        //保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //隐藏
        hideSystemUI();
        //改变屏幕亮度
        if (!config.isSystemLight()) {
            BrightnessUtils.setBrightness(this, (int)config.getLight());
        }
        //获取intent中的携带的信息
        Intent intent = getIntent();
        bookMeta = (BookMeta) intent.getSerializableExtra(EXTRA_BOOK);

        bookpage.setPageMode(config.getPageMode());
        pageFactory.setPageWidget(bookpage);

        try {
            pageFactory.openBook(bookMeta);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "打开电子书失败", Toast.LENGTH_SHORT).show();
        }

        initDayOrNight();
        initView();

        initBaiduTTs();
    }

    private void initView() {
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
        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float pro;

            // 触发操作，拖动
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pro = (float) (progress / 10000.0);
                showProgress(pro);
            }

            // 表示进度条刚开始拖动，开始拖动时候触发的操作
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            // 停止拖动时候
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pageFactory.changeProgress(pro);
            }
        });

        mPageModeDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hideSystemUI();
            }
        });

        mPageModeDialog.setPageModeListener(new PageModeDialog.PageModeListener() {
            @Override
            public void changePageMode(int pageMode) {
                bookpage.setPageMode(pageMode);
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
            public void changeBookBg(int type) {
                pageFactory.changeBookBg(type);
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

        bookpage.setTouchListener(new PageWidget.TouchListener() {
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
            if (mPageModeDialog.isShowing()) {
                mPageModeDialog.hide();
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
            if (pageFactory.getCurrentPage() != null) {
                List<BookMark> bookMarkList = LitePal.where("bookPath = ? and begin = ?", pageFactory.getBookPath(), pageFactory.getCurrentPage().getBegin() + "").find(BookMark.class);

                if (!bookMarkList.isEmpty()) {
                    Toast.makeText(ReadActivity.this, "该书签已存在", Toast.LENGTH_SHORT).show();
                } else {
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

                        Toast.makeText(ReadActivity.this, "书签添加成功", Toast.LENGTH_SHORT).show();
                    } catch (SQLException e) {
                        Toast.makeText(ReadActivity.this, "该书签已存在", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(ReadActivity.this, "添加书签失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else if (id == R.id.action_read_book) {
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

        return super.onOptionsItemSelected(item);
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
     * 隐藏菜单。沉浸式阅读
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

    //显示书本进度
    public void showProgress(float progress) {
        if (rl_progress.getVisibility() != View.VISIBLE) {
            rl_progress.setVisibility(View.VISIBLE);
        }
        setProgress(progress);
    }

    //隐藏书本进度
    public void hideProgress() {
        rl_progress.setVisibility(View.GONE);
    }

    public void initDayOrNight() {
        mDayOrNight = config.getDayOrNight();
        if (mDayOrNight) {
            tv_dayornight.setText(getResources().getString(R.string.read_setting_day));
        } else {
            tv_dayornight.setText(getResources().getString(R.string.read_setting_night));
        }
    }

    //改变显示模式
    public void changeDayOrNight() {
        if (mDayOrNight) {
            mDayOrNight = false;
            tv_dayornight.setText(getResources().getString(R.string.read_setting_night));
        } else {
            mDayOrNight = true;
            tv_dayornight.setText(getResources().getString(R.string.read_setting_day));
        }
        config.setDayOrNight(mDayOrNight);
        pageFactory.setDayOrNight(mDayOrNight);
    }

    private void setProgress(float progress) {
        DecimalFormat decimalFormat = new DecimalFormat("00.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(progress * 100.0);//format 返回的是字符串
        tv_progress.setText(p + "%");
    }

    public void setSeekBarProgress(float progress) {
        sb_progress.setProgress((int) (progress * 10000));
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
     * 注意此处为了说明流程，故意在UI线程中调用。
     * 实际集成中，该方法一定在新线程中调用，并且该线程不能结束。具体可以参考NonBlockSyntherizer的写法
     */
    private void initBaiduTTs() {
        LoggerProxy.printable(true); // 日志打印在logcat中

        // 1. 获取实例
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(this);

        // 2. 设置listener
        mSpeechSynthesizer.setSpeechSynthesizerListener(this);

        // 3. 设置appId，appKey.secretKey
        int result = mSpeechSynthesizer.setAppId("25367863");
        result = mSpeechSynthesizer.setApiKey("atqtokGtwgi8GIVkYMxGClnZ", "Of6IheDaVL6547r4HL6kv5WhpwX3NGsA");

        // 5. 以下setParam 参数选填。不填写则默认值生效
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声  3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-15 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-15 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-15 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");

        // x. 额外 ： 自动so文件是否复制正确及上面设置的参数
        Map<String, String> params = new HashMap<>();
        // 复制下上面的 mSpeechSynthesizer.setParam参数

        // 6. 初始化
        result = mSpeechSynthesizer.initTts(TtsMode.ONLINE);

    }

    @OnClick({R.id.tv_progress, R.id.rl_progress, R.id.tv_pre, R.id.sb_progress, R.id.tv_next, R.id.tv_directory, R.id.tv_dayornight, R.id.tv_pagemode, R.id.tv_setting, R.id.bookpop_bottom, R.id.rl_bottom, R.id.tv_stop_read})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_progress:
                break;

            case R.id.rl_progress:
                break;

            case R.id.tv_pre:
                pageFactory.preChapter();
                break;

            case R.id.sb_progress:
                break;

            case R.id.tv_next:
                pageFactory.nextChapter();
                break;

            case R.id.tv_directory:
                Intent intent = new Intent(ReadActivity.this, BookMarkActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_dayornight:
                changeDayOrNight();
                break;

            case R.id.tv_pagemode:
                hideReadSetting();
                mPageModeDialog.show();
                break;

            case R.id.tv_setting:
                hideReadSetting();
                mSettingDialog.show();
                break;

            case R.id.bookpop_bottom:
                break;

            case R.id.rl_bottom:
                break;

            case R.id.tv_stop_read:
                if (mSpeechSynthesizer != null) {
                    mSpeechSynthesizer.stop();
                    isSpeaking = false;
                    hideReadSetting();
                }
                break;
        }
    }

    @Override
    public void onSynthesizeStart(String s) {
        Log.d(LOG_TAG, "onSynthesizeStart, s=" + s);

    }

    /**
     * 合成数据和进度的回调接口，分多次回调
     *
     * @param utteranceId
     * @param data        合成的音频数据。该音频数据是采样率为16K，2字节精度，单声道的pcm数据。
     * @param progress    文本按字符划分的进度，比如:你好啊 进度是0-3
     */
    @Override
    public void onSynthesizeDataArrived(String utteranceId, byte[] data, int progress, int engineType) {
        Log.d(LOG_TAG, "onSynthesizeDataArrived, progress=" + progress);

    }

    /**
     * 合成正常结束，每句合成正常结束都会回调，如果过程中出错，则回调onError，不再回调此接口
     *
     * @param utteranceId
     */
    @Override
    public void onSynthesizeFinish(String utteranceId) {
        Log.d(LOG_TAG, "onSynthesizeFinish, utteranceId=" + utteranceId);

    }

    /**
     * 播放开始，每句播放开始都会回调
     *
     * @param utteranceId
     */
    @Override
    public void onSpeechStart(String utteranceId) {
        Log.d(LOG_TAG, "onSpeechStart, utteranceId=" + utteranceId);

    }

    /**
     * 播放进度回调接口，分多次回调
     *
     * @param utteranceId
     * @param progress    文本按字符划分的进度，比如:你好啊 进度是0-3
     */
    @Override
    public void onSpeechProgressChanged(String utteranceId, int progress) {
        Log.d(LOG_TAG, "onSpeechProgressChanged, utteranceId=" + utteranceId);

    }

    /**
     * 播放正常结束，每句播放正常结束都会回调，如果过程中出错，则回调onError,不再回调此接口
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
                Toast.makeText(ReadActivity.this, "小说已经读完了", Toast.LENGTH_SHORT);
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
     * 当合成或者播放过程中出错时回调此接口
     *
     * @param utteranceId
     * @param error       包含错误码和错误信息
     */
    @Override
    public void onError(String utteranceId, SpeechError error) {
        Log.e(LOG_TAG, "onError, utteranceId=" + utteranceId + ", error=" + error.code + "," + error.description);

        mSpeechSynthesizer.stop();
        isSpeaking = false;
    }
}
