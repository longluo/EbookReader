package com.longluo.ebookreader.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.longluo.ebookreader.R;
import com.longluo.ebookreader.base.BaseActivity;
import com.longluo.ebookreader.util.CommonUtils;

import butterknife.BindView;
import butterknife.OnClick;


public class AboutActivity extends BaseActivity {
    @BindView(R.id.bannner)
    ImageView bannner;

    @BindView(R.id.tv_version)
    TextView tvVersion;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;

    @BindView(R.id.app_bar)
    AppBarLayout appBar;

    @BindView(R.id.coord)
    CoordinatorLayout coord;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_about;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        toolbarLayout.setTitle(getResources().getString(R.string.app_name));
        tvVersion.setText(String.format("当前版本: %s (Build %s)", CommonUtils.getVersion(this), CommonUtils.getVersionCode(this)));
    }

    @Override
    protected void initListener() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.bannner, R.id.tv_version, R.id.toolbar, R.id.toolbar_layout, R.id.app_bar, R.id.coord})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bannner:
                break;

            case R.id.tv_version:
                break;

            case R.id.toolbar:
                break;

            case R.id.toolbar_layout:
                break;

            case R.id.app_bar:
                break;

            case R.id.coord:
                break;

            default:
                break;
        }
    }
}
