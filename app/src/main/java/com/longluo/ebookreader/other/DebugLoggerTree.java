package com.longluo.ebookreader.other;

import android.os.Build;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

/**
 * 自定义日志打印规则
 */
public final class DebugLoggerTree extends Timber.DebugTree {

    private static final int MAX_TAG_LENGTH = 23;

    /**
     * 创建日志堆栈 TAG
     */
    @Override
    protected String createStackElementTag(@NotNull StackTraceElement element) {
        String tag = "(" + element.getFileName() + ":" + element.getLineNumber() + ")";
        // 日志 TAG 长度限制已经在 Android 8.0 被移除
        if (tag.length() <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return tag;
        }
        return tag.substring(0, MAX_TAG_LENGTH);
    }
}