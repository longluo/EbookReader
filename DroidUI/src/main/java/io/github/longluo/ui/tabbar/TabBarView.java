package io.github.longluo.ui.tabbar;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import java.util.ArrayList;
import java.util.List;

import io.github.longluo.ui.R;
import io.github.longluo.ui.tabbar.interfaces.OnTabChangeListener;
import io.github.longluo.ui.tabbar.util.NavigationBarUtil;


public class TabBarView extends LinearLayout {

    private OnTabChangeListener onTabChangeListener;
    private int focusIndex = 0;

    private int height = 144;

    private int tabPaddingVertical;         //tab按钮上下内边距
    private int iconPadding;                //图标内边距
    private int textSize;                   //文本字号
    private int focusColor;                 //处于焦点状态的颜色
    private int normalColor;                //处于普通状态的颜色
    private int tabClickBackground;         //tab按钮按下效果可选值
    private boolean paddingNavigationBar;   //padding底部导航栏高度
    private boolean noDyeing;               //不使用颜色渲染（即使用图表本身的颜色）
    private boolean noSelect;               //不选中模式
    private int splitLine;                  //分割线（资源id）
    private int unreadBackground;           //未读标记背景
    private boolean focusBold;              //选中加粗

    public enum TabClickBackgroundValue {
        RIPPLE,                         //矩形水波纹
        RIPPLE_OUTSIDE,                 //外部弧形水波纹
        GRAY,                           //纯灰色
        EMPTY                           //空
    }

    private Context context;

    protected List<View> tabViews;

    public TabBarView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TabBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
        laodAttrs(context, attrs);
    }

    public TabBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
        laodAttrs(context, attrs);
    }

    private void laodAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabBar);
        height = typedArray.getLayoutDimension(R.styleable.TabBar_android_layout_height, -2);

        iconPadding = typedArray.getDimensionPixelOffset(R.styleable.TabBar_iconPadding, iconPadding);
        textSize = typedArray.getDimensionPixelOffset(R.styleable.TabBar_textSize, textSize);
        tabPaddingVertical = typedArray.getDimensionPixelOffset(R.styleable.TabBar_tabPaddingVertical, tabPaddingVertical);
        focusColor = typedArray.getColor(R.styleable.TabBar_focusColor, focusColor);
        normalColor = typedArray.getColor(R.styleable.TabBar_normalColor, normalColor);
        tabClickBackground = typedArray.getInt(R.styleable.TabBar_tabClickBackground, TabClickBackgroundValue.RIPPLE.ordinal());
        paddingNavigationBar = typedArray.getBoolean(R.styleable.TabBar_paddingNavigationBar, paddingNavigationBar);
        noDyeing = typedArray.getBoolean(R.styleable.TabBar_noDyeing, noDyeing);
        noSelect = typedArray.getBoolean(R.styleable.TabBar_noSelect, noSelect);
        focusBold = typedArray.getBoolean(R.styleable.TabBar_focusBold, noSelect);
        splitLine = typedArray.getResourceId(R.styleable.TabBar_splitLine, splitLine);
        unreadBackground = typedArray.getResourceId(R.styleable.TabBar_unreadBackground, unreadBackground);

        typedArray.recycle();
    }

    private void init() {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(HORIZONTAL);
        //默认值
        iconPadding = dp2px(5);
        tabPaddingVertical = dp2px(5);
        textSize = dp2px(12);
        focusColor = Color.rgb(62, 120, 238);
        normalColor = Color.rgb(96, 96, 96);
    }

    private View rootView;
    private List<Tab> tabDatas;

    public TabBarView setTab(List<Tab> datas) {
        tabDatas = datas;
        removeAllViews();
        tabViews = new ArrayList<>();
        for (int i = 0; i < tabDatas.size(); i++) {
            Tab tab = tabDatas.get(i);
            final View item = LayoutInflater.from(context).inflate(R.layout.item_tab, null, false);
            item.setPadding(0, tabPaddingVertical, 0, tabPaddingVertical);

            refreshBackground(item);

            addView(item);

            ImageView imgIcon = item.findViewById(R.id.img_icon);
            RelativeLayout boxNoread = item.findViewById(R.id.box_noread);
            TextView txtNoread = item.findViewById(R.id.txt_noread);
            TextView txtName = item.findViewById(R.id.txt_name);

            imgIcon.setImageBitmap(tab.getIcon());
            txtName.setText(tab.getName());

            if (tab.getUnreadNum() != 0) {
                boxNoread.setVisibility(VISIBLE);
                int left = (int) ((height - tabPaddingVertical - textSize) * 0.55);
                boxNoread.setX(left);
                if (tab.getUnreadNum() < 0) {
                    txtNoread.setVisibility(GONE);
                } else {
                    txtNoread.setVisibility(VISIBLE);
                    if (tab.getUnreadNum() > tab.getMaxUnreadNum()) {
                        txtNoread.setText(tab.getMaxUnreadNum() + "+");
                    } else {
                        txtNoread.setText(tab.getUnreadNum() + "");
                    }
                }
            } else {
                boxNoread.setVisibility(GONE);
            }

            imgIcon.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);
            txtName.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            params.gravity = Gravity.CENTER;
            item.setLayoutParams(params);

            if (splitLine != 0) {
                if (i != tabDatas.size() - 1) {
                    ImageView imgSplitLine = new ImageView(context);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(1, FrameLayout.LayoutParams.FILL_PARENT);
                    imgSplitLine.setLayoutParams(layoutParams);
                    String resType = context.getResources().getResourceTypeName(splitLine);
                    if (resType.equals("drawable")) {
                        Drawable drawable = context.getResources().getDrawable(splitLine);
                        imgSplitLine.setImageDrawable(drawable);
                        addView(imgSplitLine);
                    }
                    if (resType.equals("color")) {
                        int color = context.getResources().getColor(splitLine);
                        imgSplitLine.setBackgroundColor(color);
                        addView(imgSplitLine);
                    }
                }
            }

            if (unreadBackground != 0) {
                boxNoread.setBackgroundResource(unreadBackground);
            }
            tabViews.add(item);
        }
        refreshFocusTabStatus();
        setEvents();
        return this;
    }

    private void refreshBackground(View item) {
        if (tabClickBackground == TabClickBackgroundValue.RIPPLE.ordinal()) {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
            item.setBackgroundResource(typedValue.resourceId);
        } else if (tabClickBackground == TabClickBackgroundValue.RIPPLE_OUTSIDE.ordinal()) {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, typedValue, true);
            item.setBackgroundResource(typedValue.resourceId);
        } else if (tabClickBackground == TabClickBackgroundValue.GRAY.ordinal()) {
            item.setBackgroundResource(R.drawable.tab_gray_background);
        } else {
            item.setBackgroundColor(Color.argb(0, 0, 0, 0));
        }
    }

    private void refreshFocusTabStatus() {
        if (noSelect) return;
        for (int i = 0; i < tabViews.size(); i++) {
            View item = tabViews.get(i);
            ImageView imgIcon = item.findViewById(R.id.img_icon);
            TextView txtName = item.findViewById(R.id.txt_name);

            if (i == focusIndex) {
                if (tabDatas.get(i).getFocusIcon() != null) {
                    imgIcon.setImageBitmap(tabDatas.get(i).getFocusIcon());
                } else {
                    imgIcon.setImageBitmap(tabDatas.get(i).getIcon());
                }
                setImageViewColor(imgIcon, focusColor);
                txtName.setTextColor(focusColor);
                if (focusBold) {
                    txtName.getPaint().setFakeBoldText(true);
                }
            } else {
                imgIcon.setImageBitmap(tabDatas.get(i).getIcon());
                setImageViewColor(imgIcon, normalColor);
                txtName.setTextColor(normalColor);
                txtName.getPaint().setFakeBoldText(false);
            }
        }
    }

    private void setEvents() {
        for (int i = 0; i < tabViews.size(); i++) {
            View item = tabViews.get(i);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = tabViews.indexOf(v);
                    if (onTabChangeListener != null) {
                        if (!onTabChangeListener.onTabChanged(v, index)) {
                            focusIndex = index;
                            refreshFocusTabStatus();
                        }
                    } else {
                        focusIndex = index;
                        refreshFocusTabStatus();
                    }
                }
            });
        }
    }

    public OnTabChangeListener getOnTabChangeListener() {
        return onTabChangeListener;
    }

    /**
     * 设置点击Tab监听器
     *
     * @param onTabChangeListener
     */
    public TabBarView setOnTabChangeListener(OnTabChangeListener onTabChangeListener) {
        this.onTabChangeListener = onTabChangeListener;
        return this;
    }

    public int getFocusIndex() {
        return focusIndex;
    }

    /**
     * 设置默认选中的Tab
     *
     * @param focusIndex
     */
    public TabBarView setNormalFocusIndex(int focusIndex) {
        this.focusIndex = focusIndex;
        refreshFocusTabStatus();
        return this;
    }

    public TabBarView callOnTabChangeListenerChange() {
        if (onTabChangeListener != null) {
            onTabChangeListener.onTabChanged(getChildAt(focusIndex), focusIndex);
        }
        return this;
    }

    public int getTabPaddingVertical() {
        return tabPaddingVertical;
    }

    /**
     * 设置tab按钮上下内边距
     *
     * @param tabPaddingVertical (pixel)
     */
    public TabBarView setTabPaddingVertical(int tabPaddingVertical) {
        this.tabPaddingVertical = tabPaddingVertical;
        return this;
    }

    public int getIconPadding() {
        return iconPadding;
    }

    /**
     * 设置图标内边距
     *
     * @param iconPadding (pixel)
     */
    public TabBarView setIconPadding(int iconPadding) {
        this.iconPadding = iconPadding;
        return this;
    }

    public int getTextSize() {
        return textSize;
    }

    /**
     * 设置文本字号
     *
     * @param textSize (pixel)
     */
    public TabBarView setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    public int getFocusColor() {
        return focusColor;
    }

    /**
     * 设置处于焦点状态的颜色
     *
     * @param focusColor ColorInt
     */
    public TabBarView setFocusColor(@ColorInt int focusColor) {
        this.focusColor = focusColor;
        return this;
    }

    public int getNormalColor() {
        return normalColor;
    }

    public int getUnreadBackground() {
        return unreadBackground;
    }

    public void setUnreadBackground(int unreadBackgroundResId) {
        this.unreadBackground = unreadBackgroundResId;

        for (int i = 0; i < tabViews.size(); i++) {
            View item = tabViews.get(i);
            RelativeLayout boxNoread = item.findViewById(R.id.box_noread);

            if (unreadBackground != 0) {
                if (boxNoread != null) boxNoread.setBackgroundResource(unreadBackground);
            }
        }
    }

    /**
     * 设置处于普通状态的颜色
     *
     * @param normalColor ColorInt
     */
    public TabBarView setNormalColor(@ColorInt int normalColor) {
        this.normalColor = normalColor;
        return this;
    }

    public int getTabClickBackground() {
        return tabClickBackground;
    }

    /**
     * 设置Tab按下效果
     *
     * @param value {@link TabClickBackgroundValue#RIPPLE }{@link TabClickBackgroundValue#RIPPLE_OUTSIDE }{@link TabClickBackgroundValue#GRAY }{@link TabClickBackgroundValue#EMPTY }
     */
    public void setTabClickBackground(TabClickBackgroundValue value) {
        this.tabClickBackground = value.ordinal();
        if (tabViews != null && !tabViews.isEmpty()) {
            for (int i = 0; i < tabViews.size(); i++) {
                View item = tabViews.get(i);
                refreshBackground(item);
            }
        }
    }

    public boolean isNoDyeing() {
        return noDyeing;
    }

    public TabBarView setNoDyeing(boolean noDyeing) {
        this.noDyeing = noDyeing;
        return this;
    }

    public boolean isNoSelect() {
        return noSelect;
    }

    public TabBarView setNoSelect(boolean noSelect) {
        this.noSelect = noSelect;
        return this;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    public float px2dp(int pxValue) {
        return (pxValue / Resources.getSystem().getDisplayMetrics().density);
    }

    private void setImageViewColor(ImageView imageView, int color) {
        if (noDyeing) return;
        Drawable modeDrawable = imageView.getDrawable().mutate();
        Drawable temp = DrawableCompat.wrap(modeDrawable);
        ColorStateList colorStateList = ColorStateList.valueOf(color);
        DrawableCompat.setTintList(temp, colorStateList);
        imageView.setImageDrawable(temp);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (context != null) {
            if (paddingNavigationBar && !isInEditMode()) {
                int navHeight = NavigationBarUtil.getNavbarHeight((Activity) context);
                int newHeight = height + navHeight;
                setMeasuredDimension(getMeasuredWidth(), newHeight);//设置宽高
                setPadding(0, -navHeight / 2, 0, navHeight / 2);
            }
        }
    }

    public void setUnreadNum(int index, int unreadNum) {
        Tab tab = tabDatas.get(index);
        tab.setUnreadNum(unreadNum);
        View tabView = tabViews.get(index);
        RelativeLayout boxNoread = tabView.findViewById(R.id.box_noread);
        TextView txtNoread = tabView.findViewById(R.id.txt_noread);
        if (tab.getUnreadNum() != 0) {
            boxNoread.setVisibility(VISIBLE);
            int left = (int) (tabView.getWidth() / 2 + (height - tabPaddingVertical - textSize) * 0.55);
            boxNoread.setX(left);
            if (tab.getUnreadNum() < 0) {
                txtNoread.setVisibility(GONE);
            } else {
                txtNoread.setVisibility(VISIBLE);
                if (tab.getUnreadNum() > tab.getMaxUnreadNum()) {
                    txtNoread.setText(tab.getMaxUnreadNum() + "+");
                } else {
                    txtNoread.setText(tab.getUnreadNum() + "");
                }
            }
        } else {
            boxNoread.setVisibility(GONE);
        }
    }

    public View getChild(int index) {
        if (tabViews == null) return null;
        if (index < 0 || index >= tabViews.size()) return null;
        return tabViews.get(index);
    }
}
