package com.longluo.ebookreader.ui.dialog;

import android.content.Context;
import android.view.Gravity;

import com.longluo.ebookreader.R;

import io.github.longluo.base.BaseDialog;

/**
 * 可进行拷贝的副本
 */
public final class CopyDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder> {

        public Builder(Context context) {
            super(context);

            setContentView(R.layout.copy_dialog);
            setAnimStyle(BaseDialog.ANIM_BOTTOM);
            setGravity(Gravity.BOTTOM);
        }
    }
}