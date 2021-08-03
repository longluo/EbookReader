package com.longluo.zlibrary.ui.android.view;

import android.graphics.*;

import com.longluo.zlibrary.core.view.ZLView;
import com.longluo.zlibrary.ui.android.view.animation.BitmapManager;

final class BitmapManagerImpl implements BitmapManager {
	private final int SIZE = 2;
	private final Bitmap[] myBitmaps = new Bitmap[SIZE];
	private final ZLView.PageIndex[] myIndexes = new ZLView.PageIndex[SIZE];

	private int myWidth;
	private int myHeight;

	private final ZLAndroidWidget myWidget;

	BitmapManagerImpl(ZLAndroidWidget widget) {
		myWidget = widget;
	}

	void setSize(int w, int h) {
		if (myWidth != w || myHeight != h) {
			myWidth = w;
			myHeight = h;
			for (int i = 0; i < SIZE; ++i) {
				myBitmaps[i] = null;
				myIndexes[i] = null;
			}
			System.gc();
			System.gc();
			System.gc();
		}
	}

	public Bitmap getBitmap(ZLView.PageIndex index) {
		for (int i = 0; i < SIZE; ++i) {
			if (index == myIndexes[i]) {
				return myBitmaps[i];
			}
		}
		final int iIndex = getInternalIndex(index);
		myIndexes[iIndex] = index;
		if (myBitmaps[iIndex] == null) {
			try {
				myBitmaps[iIndex] = Bitmap.createBitmap(myWidth, myHeight, Bitmap.Config.RGB_565);
			} catch (OutOfMemoryError e) {
				System.gc();
				System.gc();
				myBitmaps[iIndex] = Bitmap.createBitmap(myWidth, myHeight, Bitmap.Config.RGB_565);
			}
		}
		myWidget.drawOnBitmap(myBitmaps[iIndex], index);
		return myBitmaps[iIndex];
	}

	public void drawBitmap(Canvas canvas, int x, int y, ZLView.PageIndex index, Paint paint) {
		canvas.drawBitmap(getBitmap(index), x, y, paint);
	}

	private int getInternalIndex(ZLView.PageIndex index) {
		for (int i = 0; i < SIZE; ++i) {
			if (myIndexes[i] == null) {
				return i;
			}
		}
		for (int i = 0; i < SIZE; ++i) {
			if (myIndexes[i] != ZLView.PageIndex.current) {
				return i;
			}
		}
		throw new RuntimeException("That's impossible");
	}

	void reset() {
		for (int i = 0; i < SIZE; ++i) {
			myIndexes[i] = null;
		}
	}

	void shift(boolean forward) {
		for (int i = 0; i < SIZE; ++i) {
			if (myIndexes[i] == null) {
				continue;
			}
			myIndexes[i] = forward ? myIndexes[i].getPrevious() : myIndexes[i].getNext();
		}
	}
}
