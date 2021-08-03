package com.longluo.android.ebookreader;

import android.animation.*;
import android.content.Context;
import android.util.AttributeSet;
import android.view.*;
import android.widget.*;

public class NavigationWindow extends LinearLayout {
	public NavigationWindow(Context context) {
		super(context);
	}

	public NavigationWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NavigationWindow(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	private Animator myShowHideAnimator;

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
