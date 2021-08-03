package com.longluo.android.ebookreader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class SimplePopupWindow extends FrameLayout {
	public SimplePopupWindow(Context context) {
		super(context);
	}

	public SimplePopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SimplePopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	public void show() {
		post(new Runnable() {
			public void run() {
				setVisibility(View.VISIBLE);
			}
		});
	}

	public void hide() {
		post(new Runnable() {
			public void run() {
				setVisibility(View.GONE);
			}
		});
	}
}
