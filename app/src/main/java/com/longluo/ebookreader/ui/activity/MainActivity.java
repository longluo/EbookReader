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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.longluo.ebookreader.manager.ReadSettingManager;
import com.longluo.ebookreader.R;
import com.longluo.ebookreader.base.BaseActivity;
import com.longluo.ebookreader.db.BookMeta;
import com.longluo.ebookreader.ui.adapter.ShelfAdapter;
import com.longluo.ebookreader.util.BookUtils;
import com.longluo.ebookreader.util.DisplayUtils;
import com.longluo.ebookreader.widget.view.DragGridView;
import com.longluo.ebookreader.widget.animation.ContentScaleAnimation;
import com.longluo.ebookreader.widget.animation.Rotate3DAnimation;


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
    DragGridView bookShelf;

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
//        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);//设置导航图标

        readSettingManager = ReadSettingManager.getInstance();
        // 删除窗口背景
        getWindow().setBackgroundDrawable(null);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wmRootView = new AbsoluteLayout(this);
        rootView = getWindow().getDecorView();
//        SQLiteDatabase db = Connector.getDatabase();  //初始化数据库
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
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, FileChooserActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        bookShelf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (bookMetas.size() > position) {
                    itemPosition = position;
                    String bookname = bookMetas.get(itemPosition).getBookName();
                    shelfAdapter.setItemToFirst(itemPosition);
                    final BookMeta bookMeta = bookMetas.get(itemPosition);
                    bookMeta.setId(bookMetas.get(0).getId());
                    final String path = bookMeta.getBookPath();
                    File file = new File(path);
                    if (!file.exists()) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(MainActivity.this.getString(R.string.app_name))
                                .setMessage(path + "文件不存在,是否删除该书本？")
                                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LitePal.deleteAll(BookMeta.class, "bookPath = ?", path);
                                        bookMetas = LitePal.findAll(BookMeta.class);
                                        shelfAdapter.setBookList(bookMetas);
                                    }
                                }).setCancelable(true).show();
                        return;
                    }

                    BookUtils.openBook(MainActivity.this, bookMeta);

//                    if (!isOpen){
//                        bookLists = DataSupport.findAll(BookList.class);
//                        adapter.notifyDataSetChanged();
//                    }
//                    itemTextView = (TextView) view.findViewById(R.id.tv_name);
//                    //获取item在屏幕中的x，y坐标
//                    itemTextView.getLocationInWindow(location);
//
//                    //初始化dialog
//                    mWindowManager.addView(wmRootView, getDefaultWindowParams());
//                    cover = new TextView(getApplicationContext());
//                    cover.setBackgroundDrawable(getResources().getDrawable(R.mipmap.cover_default_new));
//                    cover.setCompoundDrawablesWithIntrinsicBounds(null,null,null,getResources().getDrawable(R.mipmap.cover_type_txt));
//                    cover.setText(bookname);
//                    cover.setTextColor(getResources().getColor(R.color.read_textColor));
//                    cover.setTypeface(typeface);
//                    int coverPadding = (int) CommonUtil.convertDpToPixel(getApplicationContext(), 10);
//                    cover.setPadding(coverPadding, coverPadding, coverPadding, coverPadding);
//
//                    content = new ImageView(getApplicationContext());
//                    Bitmap contentBitmap = Bitmap.createBitmap(itemTextView.getMeasuredWidth(),itemTextView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
//                    contentBitmap.eraseColor(getResources().getColor(R.color.read_background_paperYellow));
//                    content.setImageBitmap(contentBitmap);
//
//                    AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
//                            itemTextView.getLayoutParams());
//                    params.x = location[0];
//                    params.y = location[1];
//                    wmRootView.addView(content, params);
//                    wmRootView.addView(cover, params);
//
//                    initAnimation();
//                    if (contentAnimation.getMReverse()) {
//                        contentAnimation.reverse();
//                    }
//                    if (coverAnimation.getMReverse()) {
//                        coverAnimation.reverse();
//                    }
//                    cover.clearAnimation();
//                    cover.startAnimation(coverAnimation);
//                    content.clearAnimation();
//                    content.startAnimation(contentAnimation);
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DragGridView.setIsShowDeleteButton(false);
        bookMetas = LitePal.findAll(BookMeta.class);
        shelfAdapter.setBookList(bookMetas);
        closeBookAnimation();
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

    public void closeBookAnimation() {
        if (mIsOpen && wmRootView != null) {
            //因为书本打开后会移动到第一位置，所以要设置新的位置参数
            contentAnimation.setmPivotXValue(bookShelf.getFirstLocation()[0]);
            contentAnimation.setmPivotYValue(bookShelf.getFirstLocation()[1]);
            coverAnimation.setmPivotXValue(bookShelf.getFirstLocation()[0]);
            coverAnimation.setmPivotYValue(bookShelf.getFirstLocation()[1]);

            AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
                    itemTextView.getLayoutParams());
            params.x = bookShelf.getFirstLocation()[0];
            params.y = bookShelf.getFirstLocation()[1];//firstLocation[1]在滑动的时候回改变,所以要在dispatchDraw的时候获取该位置值
            wmRootView.updateViewLayout(cover, params);
            wmRootView.updateViewLayout(content, params);
            //动画逆向运行
            if (!contentAnimation.getMReverse()) {
                contentAnimation.reverse();
            }
            if (!coverAnimation.getMReverse()) {
                coverAnimation.reverse();
            }
            //清除动画再开始动画
            content.clearAnimation();
            content.startAnimation(contentAnimation);
            cover.clearAnimation();
            cover.startAnimation(coverAnimation);
        }
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
                shelfAdapter.setItemToFirst(itemPosition);
//                bookLists = DataSupport.findAll(BookList.class);
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

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }else if (id == R.id.action_select_file){
//            Intent intent = new Intent(MainActivity.this, FileChooserActivity.class);
//            startActivity(intent);
//        }

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

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkUpdate(final boolean showMessage) {
        String url = "http://api.fir.im/apps/latest/57be8d56959d6960d5000327";
//        OkHttpUtils
//                .get()
//                .url(url)
//                .addParams("api_token", "a48b9bbcef61f34c51160bfed26aa6b2")
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        if (showMessage) {
//                            Toast.makeText(MainActivity.this, "检查更新失败！", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String version = jsonObject.getString("version");
//                            String versionCode = CommonUtil.getVersionCode(MainActivity.this) + "";
//                            if (versionCode.compareTo(version) < 0) {
//                                showUpdateDialog(jsonObject.getString("name"), jsonObject.getString("versionShort"), jsonObject.getString("changelog"), jsonObject.getString("update_url"), MainActivity.this);
//                            } else {
//                                if (showMessage) {
//                                    Toast.makeText(MainActivity.this, "已经是最新版本！", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        } catch (JSONException e) {
//                            if (showMessage) {
//                                Toast.makeText(MainActivity.this, "检查更新失败", Toast.LENGTH_SHORT).show();
//                            }
//                            e.printStackTrace();
//                        }
//                    }
//
//                });
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
