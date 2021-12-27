package com.longluo.ebookreader.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longluo.ebookreader.constant.Constants;
import com.longluo.ebookreader.manager.ReadSettingManager;
import com.longluo.ebookreader.R;
import com.longluo.ebookreader.ui.activity.MoreSettingActivity;
import com.longluo.ebookreader.ui.activity.ReadActivity;
import com.longluo.ebookreader.ui.adapter.ReadPageBgAdapter;
import com.longluo.ebookreader.ui.base.adapter.BaseListAdapter;
import com.longluo.ebookreader.util.BrightnessUtils;
import com.longluo.ebookreader.widget.page.PageMode;
import com.longluo.ebookreader.widget.page.PageStyle;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ReadSettingDialog extends Dialog {
    private static final String LOG_TAG = ReadSettingDialog.class.getSimpleName();

    @BindView(R.id.iv_read_setting_brightness_minus)
    ImageView ivBrightMinus;
    @BindView(R.id.sb_read_setting_brightness)
    SeekBar sbBrightness;
    @BindView(R.id.iv_read_setting_brightness_plus)
    ImageView ivBrightPlus;
    @BindView(R.id.cb_read_setting_brightness_auto)
    CheckBox cbBrightAuto;

    @BindView(R.id.tv_read_setting_font_minus)
    TextView mTvFontMinus;
    @BindView(R.id.tv_read_setting_font_size)
    TextView mTvFontSize;
    @BindView(R.id.tv_read_setting_font_plus)
    TextView mTvFontPlus;
    @BindView(R.id.cb_read_setting_font_default)
    CheckBox mCbFontDefault;

    @BindView(R.id.tv_font_default)
    TextView tv_default;
    @BindView(R.id.tv_qihei)
    TextView tv_qihei;
    @BindView(R.id.tv_fzxinghei)
    TextView tv_fzxinghei;
    @BindView(R.id.tv_fzkatong)
    TextView tv_fzkatong;
    @BindView(R.id.tv_bysong)
    TextView tv_bysong;

    @BindView(R.id.read_setting_rg_page_mode)
    RadioGroup mRgPageMode;
    @BindView(R.id.read_setting_rb_simulation)
    RadioButton mRbSimulation;
    @BindView(R.id.read_setting_rb_cover)
    RadioButton mRbCover;
    @BindView(R.id.read_setting_rb_slide)
    RadioButton mRbSlide;
    @BindView(R.id.read_setting_rb_scroll)
    RadioButton mRbScroll;
    @BindView(R.id.read_setting_rb_none)
    RadioButton mRbNone;

    @BindView(R.id.read_setting_rv_bg)
    RecyclerView mRvBg;

    @BindView(R.id.tv_read_setting_more)
    TextView mTvMore;

    private Context context;
    private ReadSettingManager readSettingManager;
    private boolean isSystem;
    private SettingListener mSettingListener;
    private int FONT_SIZE_MIN;
    private int FONT_SIZE_MAX;
    private int currentFontSize;

    private PageMode mPageMode;
    private ReadPageBgAdapter mPageStyleAdapter;
    private PageStyle mPageStyle;

    public ReadSettingDialog(Context context) {
        this(context, R.style.setting_dialog);
    }

    public ReadSettingDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);
        setContentView(R.layout.dialog_read_setting);
        setUpWindow();
        ButterKnife.bind(this);
        initData();
        initWidget();
        initListener();
    }

    private void setUpWindow() {
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);
    }

    private void initData() {
        readSettingManager = ReadSettingManager.getInstance();

        mPageMode = readSettingManager.getPageMode();
        mPageStyle = readSettingManager.getBookPageStyle();

        FONT_SIZE_MIN = (int) getContext().getResources().getDimension(R.dimen.reading_min_text_size);
        FONT_SIZE_MAX = (int) getContext().getResources().getDimension(R.dimen.reading_max_text_size);

        initReadBgAdapter();
        initFont();
    }

    private void initFont() {
        //初始化字体大小
        currentFontSize = readSettingManager.getTextSize();
        mTvFontSize.setText(currentFontSize + "");

        //初始化字体
        tv_default.setTypeface(readSettingManager.getTypeface(Constants.FONT_TYPE_DEFAULT));
        tv_qihei.setTypeface(readSettingManager.getTypeface(Constants.FONT_TYPE_QIHEI));
        tv_fzkatong.setTypeface(readSettingManager.getTypeface(Constants.FONT_TYPE_FZKATONG));
        tv_bysong.setTypeface(readSettingManager.getTypeface(Constants.FONT_TYPE_BYSONG));
        selectTypeface(readSettingManager.getTypefacePath());
    }

    private void initReadBgAdapter() {
        Drawable[] drawables = {
                getDrawable(R.color.read_bg_default)
                , getDrawable(R.color.read_bg_1)
                , getDrawable(R.color.read_bg_2)
                , getDrawable(R.color.read_bg_3)
                , getDrawable(R.color.read_bg_4)};

        mPageStyleAdapter = new ReadPageBgAdapter();
        mRvBg.setLayoutManager(new GridLayoutManager(getContext(), 5));
        mRvBg.setAdapter(mPageStyleAdapter);
        mPageStyleAdapter.refreshItems(Arrays.asList(drawables));
        mPageStyleAdapter.setPageStyleChecked(mPageStyle);
    }

    private Drawable getDrawable(int drawRes) {
        return ContextCompat.getDrawable(getContext(), drawRes);
    }

    private void initWidget() {
        cbBrightAuto.setChecked(isSystem);

        initPageMode(mPageMode);
    }

    private void initPageMode(PageMode pageMode) {
        switch (pageMode) {
            case MODE_SIMULATION:
                mRbSimulation.setChecked(true);
                break;

            case MODE_COVER:
                mRbCover.setChecked(true);
                break;

            case MODE_SLIDE:
                mRbSlide.setChecked(true);
                break;

            case MODE_NONE:
                mRbNone.setChecked(true);
                break;

            default:
                break;
        }
    }

    private void initListener() {
        ivBrightMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbBrightAuto.isChecked()) {
                    cbBrightAuto.setChecked(false);
                }
                int progress = sbBrightness.getProgress() - 1;
                if (progress < 0) {
                    return;
                }
                sbBrightness.setProgress(progress);
                BrightnessUtils.setBrightness((Activity) context, progress);
            }
        });

        ivBrightPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbBrightAuto.isChecked()) {
                    cbBrightAuto.setChecked(false);
                }
                int progress = sbBrightness.getProgress() + 1;
                if (progress > sbBrightness.getMax()) return;
                sbBrightness.setProgress(progress);
                BrightnessUtils.setBrightness((Activity) context, progress);
                ReadSettingManager.getInstance().setBrightness(progress);
            }
        });

        sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 10) {
                    changeBright(false, progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cbBrightAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                    if (isChecked) {
//                        BrightnessUtils.setBrightness((Activity) context, BrightnessUtils.getScreenBrightness((Activity) context));
//                    } else {
//                        //获取进度条的亮度
//                        BrightnessUtils.setBrightness((Activity) context, sbBrightness.getProgress());
//                    }

                isSystem = !isSystem;
                changeBright(isSystem, sbBrightness.getProgress());

                ReadSettingManager.getInstance().setAutoBrightness(isChecked);
            }
        });

        mTvFontMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reduceFontSize();
            }
        });

        mTvFontPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                raiseFontSize();
            }
        });

        mCbFontDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                defaultFontSize(isChecked);
            }
        });

        mRgPageMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                PageMode pageMode;
                switch (checkedId) {
                    case R.id.read_setting_rb_simulation:
                        pageMode = PageMode.MODE_SIMULATION;
                        break;
                    case R.id.read_setting_rb_cover:
                        pageMode = PageMode.MODE_COVER;
                        break;
                    case R.id.read_setting_rb_slide:
                        pageMode = PageMode.MODE_SLIDE;
                        break;
                    case R.id.read_setting_rb_scroll:
                        pageMode = PageMode.MODE_SCROLL;
                        break;
                    case R.id.read_setting_rb_none:
                        pageMode = PageMode.MODE_NONE;
                        break;

                    default:
                        pageMode = PageMode.MODE_SIMULATION;
                        break;
                }

                readSettingManager.setPageMode(pageMode);
                mSettingListener.changePageMode(pageMode);
            }
        });

        mPageStyleAdapter.setOnItemClickListener(new BaseListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                setBookPageStyle(PageStyle.values()[pos]);
            }
        });
    }

    public void setBookPageStyle(PageStyle pageStyle) {
        readSettingManager.setBookPageStyle(pageStyle);
        if (mSettingListener != null) {
            mSettingListener.changeBookPageStyle(pageStyle);
        }
    }

    //选择字体
    private void selectTypeface(String typeface) {
        if (typeface.equals(Constants.FONT_TYPE_DEFAULT)) {
            setTextViewSelect(tv_default, true);
            setTextViewSelect(tv_qihei, false);
            setTextViewSelect(tv_fzxinghei, false);
            setTextViewSelect(tv_fzkatong, false);
            setTextViewSelect(tv_bysong, false);
//            setTextViewSelect(tv_xinshou, false);
//            setTextViewSelect(tv_wawa, false);
        } else if (typeface.equals(Constants.FONT_TYPE_QIHEI)) {
            setTextViewSelect(tv_default, false);
            setTextViewSelect(tv_qihei, true);
            setTextViewSelect(tv_fzxinghei, false);
            setTextViewSelect(tv_fzkatong, false);
            setTextViewSelect(tv_bysong, false);
//            setTextViewSelect(tv_xinshou, false);
//            setTextViewSelect(tv_wawa, false);
        } else if (typeface.equals(Constants.FONT_TYPE_FZXINGHEI)) {
            setTextViewSelect(tv_default, false);
            setTextViewSelect(tv_qihei, false);
            setTextViewSelect(tv_fzxinghei, true);
            setTextViewSelect(tv_fzkatong, false);
            setTextViewSelect(tv_bysong, false);
        } else if (typeface.equals(Constants.FONT_TYPE_FZKATONG)) {
            setTextViewSelect(tv_default, false);
            setTextViewSelect(tv_qihei, false);
            setTextViewSelect(tv_fzxinghei, false);
            setTextViewSelect(tv_fzkatong, true);
            setTextViewSelect(tv_bysong, false);
//            setTextViewSelect(tv_xinshou, false);
//            setTextViewSelect(tv_wawa, true);
        } else if (typeface.equals(Constants.FONT_TYPE_BYSONG)) {
            setTextViewSelect(tv_default, false);
            setTextViewSelect(tv_qihei, false);
            setTextViewSelect(tv_fzxinghei, false);
            setTextViewSelect(tv_fzkatong, false);
            setTextViewSelect(tv_bysong, true);
//            setTextViewSelect(tv_xinshou, false);
//            setTextViewSelect(tv_wawa, true);
        }
    }

    //设置字体
    public void setTypeface(String typeface) {
        readSettingManager.setTypeface(typeface);
        Typeface tface = readSettingManager.getTypeface(typeface);
        if (mSettingListener != null) {
            mSettingListener.changeTypeFace(tface);
        }
    }

    //设置亮度
    public void setBrightness(float brightness) {
        sbBrightness.setProgress((int) (brightness * 255));
    }

    //设置按钮选择的背景
    private void setTextViewSelect(TextView textView, Boolean isSelect) {
        if (isSelect) {
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_select_bg));
            textView.setTextColor(getContext().getResources().getColor(R.color.read_dialog_button_select));
        } else {
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_bg));
            textView.setTextColor(getContext().getResources().getColor(R.color.white));
        }
    }

    private void applyCompat() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
    }

    public Boolean isShow() {
        return isShowing();
    }


    @OnClick({R.id.tv_qihei, R.id.tv_fzxinghei, R.id.tv_fzkatong, R.id.tv_bysong,
            R.id.tv_font_default, R.id.tv_read_setting_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_qihei:
                selectTypeface(Constants.FONT_TYPE_QIHEI);
                setTypeface(Constants.FONT_TYPE_QIHEI);
                break;

            case R.id.tv_fzxinghei:
                selectTypeface(Constants.FONT_TYPE_FZXINGHEI);
                setTypeface(Constants.FONT_TYPE_FZXINGHEI);
                break;

            case R.id.tv_fzkatong:
                selectTypeface(Constants.FONT_TYPE_FZKATONG);
                setTypeface(Constants.FONT_TYPE_FZKATONG);
                break;

            case R.id.tv_bysong:
                selectTypeface(Constants.FONT_TYPE_BYSONG);
                setTypeface(Constants.FONT_TYPE_BYSONG);
                break;

            case R.id.tv_font_default:
                selectTypeface(Constants.FONT_TYPE_DEFAULT);
                setTypeface(Constants.FONT_TYPE_DEFAULT);
                break;

            case R.id.tv_read_setting_more:
                startMoreSettingActivity();
                break;

            default:
                break;
        }
    }

    private void raiseFontSize() {
        if (mCbFontDefault.isChecked()) {
            mCbFontDefault.setChecked(false);
        }
        if (currentFontSize < FONT_SIZE_MAX) {
            currentFontSize += 1;
            mTvFontSize.setText(currentFontSize + "");
            readSettingManager.setTextSize(currentFontSize);
            if (mSettingListener != null) {
                mSettingListener.changeFontSize(currentFontSize);
            }
        }
    }

    private void defaultFontSize(boolean isChecked) {
        if (isChecked) {
            currentFontSize = (int) getContext().getResources().getDimension(R.dimen.reading_default_text_size);
            mTvFontSize.setText(currentFontSize + "");
            readSettingManager.setTextSize(currentFontSize);
            if (mSettingListener != null) {
                mSettingListener.changeFontSize(currentFontSize);
            }
        }
    }

    private void reduceFontSize() {
        if (mCbFontDefault.isChecked()) {
            mCbFontDefault.setChecked(false);
        }
        if (currentFontSize > FONT_SIZE_MIN) {
            currentFontSize -= 1;
            mTvFontSize.setText(currentFontSize + "");
            readSettingManager.setTextSize(currentFontSize);
            if (mSettingListener != null) {
                mSettingListener.changeFontSize(currentFontSize);
            }
        }
    }

    //改变亮度
    public void changeBright(Boolean isSystem, int brightness) {
        float light = (float) (brightness / 255.0);
//        setTextViewSelect(tv_xitong, isSystem);
        readSettingManager.setSystemLight(isSystem);
        readSettingManager.setLight(light);
        if (mSettingListener != null) {
            mSettingListener.changeSystemBright(isSystem, light);
        }
    }

    private void startMoreSettingActivity() {
        Intent intent = new Intent(context, MoreSettingActivity.class);
        ((Activity) context).startActivityForResult(intent, ReadActivity.REQUEST_MORE_SETTING);
        dismiss();
    }

    public void setSettingListener(SettingListener settingListener) {
        this.mSettingListener = settingListener;
    }

    public interface SettingListener {
        void changeSystemBright(Boolean isSystem, float brightness);

        void changeFontSize(int fontSize);

        void changeTypeFace(Typeface typeface);

        void changeBookPageStyle(PageStyle pageStyle);

        void changePageMode(PageMode pageMode);
    }

}