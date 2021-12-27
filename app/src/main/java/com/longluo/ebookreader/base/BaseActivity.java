package com.longluo.ebookreader.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.longluo.ebookreader.util.StatusBarCompat;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity {
    private static final int INVALID_VAL = -1;

    private ProgressDialog mProgressDialog;

    protected CompositeDisposable mDisposable;

    private Toolbar mToolbar;

    public abstract int getLayoutRes();

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void initListener();

    protected void addDisposable(Disposable d) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }

        mDisposable.add(d);
    }

    protected void setUpToolbar(Toolbar toolbar) {

    }

    private void initToolbar() {
        if (mToolbar != null) {
            supportActionBar(mToolbar);
            setUpToolbar(mToolbar);
        }
    }

    protected void initWidget() {

    }

    protected void processLogic() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        ButterKnife.bind(this);
        initData(savedInstanceState);
        initToolbar();
        initWidget();
        initListener();
        processLogic();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    protected void startActivity(Class<? extends AppCompatActivity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    protected ActionBar supportActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(
                (v) -> finish()
        );

        return actionBar;
    }

    protected void setStatusBarColor(int statusColor) {
        StatusBarCompat.compat(this, ContextCompat.getColor(this, statusColor));
    }

    public void showProgress(boolean flag, String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(flag);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage(message);
        }

        mProgressDialog.show();
    }

    public void hideProgress() {
        if (mProgressDialog == null)
            return;

        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 检查是否拥有权限
     *
     * @param activity
     * @param permission
     * @param requestCode
     * @param errorText
     */
    protected void checkPermission(Activity activity, String permission, int requestCode, String errorText) {
        //判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permission)) {
                Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
                //进行权限请求
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            } else {
                //进行权限请求
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
        }
    }
}
