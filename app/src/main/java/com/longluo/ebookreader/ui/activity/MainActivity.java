package com.longluo.ebookreader.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.longluo.ebookreader.R;
import com.longluo.ebookreader.base.BaseActivity;
import com.longluo.ebookreader.db.BookMeta;
import com.longluo.ebookreader.manager.ReadSettingManager;
import com.longluo.ebookreader.ui.adapter.ShelfAdapter;
import com.longluo.ebookreader.util.BookUtils;
import com.longluo.ebookreader.util.DisplayUtils;
import com.longluo.ebookreader.widget.animation.ContentScaleAnimation;
import com.longluo.ebookreader.widget.animation.Rotate3DAnimation;
import com.longluo.ebookreader.widget.view.DragGridView;

import org.litepal.LitePal;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, Animation.AnimationListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.bookShelf)
    RecyclerView bookShelf;

    private WindowManager mWindowManager;
    private AbsoluteLayout wmRootView;
    private View rootView;
    private Typeface typeface;

    private List<BookMeta> bookMetas;
    private ShelfAdapter shelfAdapter;

    //点击书本的位置
    private int itemPosition;
    private TextView itemTextView;
    //点击书本在屏幕中的x，y坐标
    private int[] location = new int[2];

    private static TextView cover;
    private static ImageView content;

    //书本打开动画缩放比例
    private float scaleTimes;
    //书本打开缩放动画
    private static ContentScaleAnimation contentAnimation;
    private static Rotate3DAnimation coverAnimation;
    //书本打开缩放动画持续时间
    public static final int ANIMATION_DURATION = 800;
    //打开书本的第一个动画是否完成
    private boolean mIsOpen = false;
    //动画加载计数器  0 默认  1一个动画执行完毕   2二个动画执行完毕
    private int animationCount = 0;

    private static Boolean isExit = false;

    private ReadSettingManager readSettingManager;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);

        readSettingManager = ReadSettingManager.getInstance();
        // 删除窗口背景
        getWindow().setBackgroundDrawable(null);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wmRootView = new AbsoluteLayout(this);
        rootView = getWindow().getDecorView();
        typeface = readSettingManager.getTypeface();
        bookMetas = LitePal.findAll(BookMeta.class);

        shelfAdapter = new ShelfAdapter(MainActivity.this, bookMetas);
        bookShelf.setAdapter(shelfAdapter);
    }

    @Override
    protected void initListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FileChooserActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DragGridView.setIsShowDeleteButton(false);
        bookMetas = LitePal.findAll(BookMeta.class);
        shelfAdapter.setBookLists(bookMetas);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        DragGridView.setIsShowDeleteButton(false);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        DragGridView.setIsShowDeleteButton(false);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawer.isDrawerOpen(Gravity.LEFT)) {
                drawer.closeDrawers();
            } else {
                exitBy2Click();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 在2秒内按下返回键两次才退出
     */
    private void exitBy2Click() {
        // press twice to exit
        Timer tExit;
        if (!isExit) {
            isExit = true; // ready to exit
            if (DragGridView.getShowDeleteButton()) {
                DragGridView.setIsShowDeleteButton(false);
                //要保证是同一个adapter对象,否则在Restart后无法notifyDataSetChanged
                shelfAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, this.getResources().getString(R.string.press_twice_to_exit), Toast.LENGTH_SHORT).show();
            }
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // cancel exit
                }
            }, 2000); // 2 seconds cancel exit task

        } else {
            finish();
            // call fragments and end streams and services
            System.exit(0);
        }
    }

    //初始化dialog动画
    private void initAnimation() {
        AccelerateInterpolator interpolator = new AccelerateInterpolator();

        float scale1 = DisplayUtils.getScreenWidthPixels(this) / (float) itemTextView.getMeasuredWidth();
        float scale2 = DisplayUtils.getScreenHeightPixels(this) / (float) itemTextView.getMeasuredHeight();
        scaleTimes = scale1 > scale2 ? scale1 : scale2;  //计算缩放比例

        contentAnimation = new ContentScaleAnimation(location[0], location[1], scaleTimes, false);
        contentAnimation.setInterpolator(interpolator);  //设置插值器
        contentAnimation.setDuration(ANIMATION_DURATION);
        contentAnimation.setFillAfter(true);  //动画停留在最后一帧
        contentAnimation.setAnimationListener(this);

        coverAnimation = new Rotate3DAnimation(0, -180, location[0], location[1], scaleTimes, false);
        coverAnimation.setInterpolator(interpolator);
        coverAnimation.setDuration(ANIMATION_DURATION);
        coverAnimation.setFillAfter(true);
        coverAnimation.setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //有两个动画监听会执行两次，所以要判断
        if (!mIsOpen) {
            animationCount++;
            if (animationCount >= 2) {
                mIsOpen = true;
                BookMeta bookMeta = bookMetas.get(itemPosition);
                bookMeta.setId(bookMetas.get(0).getId());
                BookUtils.openBook(MainActivity.this, bookMeta);
            }

        } else {
            animationCount--;
            if (animationCount <= 0) {
                mIsOpen = false;
                wmRootView.removeView(cover);
                wmRootView.removeView(content);
                mWindowManager.removeView(wmRootView);
            }
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    //获取dialog属性
    private WindowManager.LayoutParams getDefaultWindowParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                0, 0,
                WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,//windown类型,有层级的大的层级会覆盖在小的层级
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                PixelFormat.RGBA_8888);

        return params;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_select_file) {
            Intent intent = new Intent(MainActivity.this, FileChooserActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feedback) {
//            FeedbackAgent agent = new FeedbackAgent(this);
//            agent.startFeedbackActivity();
        } else if (id == R.id.nav_checkupdate) {
            checkUpdate(true);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public void checkUpdate(final boolean showMessage) {
        String url = "http://api.fir.im/apps/latest/57be8d56959d6960d5000327";
    }

    public static void showUpdateDialog(final String name, String version, String changelog, final String updateUrl, final Context context) {
        String title = "发现新版" + name + "，版本号：" + version;

        new AlertDialog.Builder(context).setTitle(title)
                .setMessage(changelog)
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse(updateUrl);   //指定网址
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);           //指定Action
                        intent.setData(uri);                            //设置Uri
                        context.startActivity(intent);        //启动Activity
                    }
                })
                .show();
    }
}
