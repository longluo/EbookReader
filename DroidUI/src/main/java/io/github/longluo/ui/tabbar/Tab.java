package io.github.longluo.ui.tabbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

public class Tab {
    
    private String name;
    private Bitmap icon;
    private int unreadNum;
    private int maxUnreadNum = 999;
    private Bitmap focusIcon;
    
    public Tab(@Nullable Context context, @Nullable String name, @DrawableRes int resId) {
        setName(name);
        setIcon(context, resId);
    }
    
    public Tab(@Nullable String name, @Nullable Drawable icon) {
        setName(name);
        setIcon(icon);
    }
    
    public Tab(@Nullable String name, @Nullable Bitmap icon) {
        setName(name);
        setIcon(icon);
    }
    
    public Tab(@Nullable Context context, @Nullable String name, @DrawableRes int resId, @DrawableRes int focusResId) {
        setName(name);
        setIcon(context, resId);
        setFocusIcon(context, focusResId);
    }
    
    public Tab(@Nullable String name, @Nullable Drawable icon, @Nullable Drawable focusIcon) {
        setName(name);
        setIcon(icon);
        setFocusIcon(focusIcon);
    }
    
    public Tab(@Nullable String name, @Nullable Bitmap icon, @Nullable Bitmap focusIcon) {
        setName(name);
        setIcon(icon);
        setFocusIcon(focusIcon);
    }
    
    public String getName() {
        return name;
    }
    
    public Tab setName(@Nullable String name) {
        this.name = name;
        return this;
    }
    
    public Bitmap getIcon() {
        return icon;
    }
    
    public Tab setIcon(@Nullable Bitmap icon) {
        this.icon = icon;
        return this;
    }
    
    public Tab setIcon(@Nullable Context context, @DrawableRes int resId) {
        this.icon = BitmapFactory.decodeResource(context.getResources(), resId);
        return this;
    }
    
    public Tab setIcon(@Nullable Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        this.icon = bd.getBitmap();
        return this;
    }
    
    public int getUnreadNum() {
        return unreadNum;
    }
    
    public Tab setUnreadNum(int unreadNum) {
        this.unreadNum = unreadNum;
        return this;
    }
    
    public int getMaxUnreadNum() {
        return maxUnreadNum;
    }
    
    public Tab setMaxUnreadNum(int maxUnreadNum) {
        this.maxUnreadNum = maxUnreadNum;
        return this;
    }
    
    public Bitmap getFocusIcon() {
        return focusIcon;
    }
    
    public Tab setFocusIcon(@Nullable Bitmap icon) {
        this.focusIcon = icon;
        return this;
    }
    
    public Tab setFocusIcon(@Nullable Context context, @DrawableRes int resId) {
        this.focusIcon = BitmapFactory.decodeResource(context.getResources(), resId);
        return this;
    }
    
    public Tab setFocusIcon(@Nullable Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        this.focusIcon = bd.getBitmap();
        return this;
    }
}
