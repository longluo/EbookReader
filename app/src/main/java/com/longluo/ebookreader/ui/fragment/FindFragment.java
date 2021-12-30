package com.longluo.ebookreader.ui.fragment;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.longluo.ebookreader.R;
import com.longluo.ebookreader.aop.SingleClick;
import com.longluo.ebookreader.app.TitleBarFragment;
import com.longluo.ebookreader.ui.activity.HomeActivity;

import io.github.longluo.ui.view.CountdownView;
import io.github.longluo.ui.view.SwitchButton;

/**
 * 发现 Fragment
 */
public final class FindFragment extends TitleBarFragment<HomeActivity>
        implements SwitchButton.OnCheckedChangeListener {

    private ImageView mCircleView;
    private ImageView mCornerView;
    private SwitchButton mSwitchButton;
    private CountdownView mCountdownView;

    public static FindFragment newInstance() {
        return new FindFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.find_fragment;
    }

    @Override
    protected void initView() {
        mCircleView = findViewById(R.id.iv_find_circle);
        mCornerView = findViewById(R.id.iv_find_corner);
        mSwitchButton = findViewById(R.id.sb_find_switch);
        mCountdownView = findViewById(R.id.cv_find_countdown);
        setOnClickListener(mCountdownView);

        mSwitchButton.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {
        // 显示圆形的 ImageView
        Glide.with(this)
                .load(R.drawable.update_app_top_bg)
                .transform(new MultiTransformation<>(new CenterCrop(), new CircleCrop()))
                .into(mCircleView);

        // 显示圆角的 ImageView
        Glide.with(this)
                .load(R.drawable.update_app_top_bg)
                .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners((int) getResources().getDimension(R.dimen.dp_10))))
                .into(mCornerView);
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        if (view == mCountdownView) {
            toast(R.string.common_code_send_hint);
            mCountdownView.start();
        }
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    /**
     * {@link SwitchButton.OnCheckedChangeListener}
     */

    @Override
    public void onCheckedChanged(SwitchButton button, boolean checked) {
        toast(checked);
    }
}