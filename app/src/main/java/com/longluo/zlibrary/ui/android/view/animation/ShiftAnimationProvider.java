package com.longluo.zlibrary.ui.android.view.animation;

import android.graphics.*;

import com.longluo.zlibrary.ui.android.view.ViewUtil;

public final class ShiftAnimationProvider extends SimpleAnimationProvider {
	private final Paint myPaint = new Paint();
	{
		myPaint.setColor(Color.rgb(127, 127, 127));
	}

	public ShiftAnimationProvider(BitmapManager bitmapManager) {
		super(bitmapManager);
	}

	@Override
	protected void drawInternal(Canvas canvas) {
		if (myDirection.IsHorizontal) {
			final int dX = myEndX - myStartX;
			drawBitmapTo(canvas, dX > 0 ? dX - myWidth : dX + myWidth, 0, myPaint);
			drawBitmapFrom(canvas, dX, 0, myPaint);
			if (dX > 0 && dX < myWidth) {
				canvas.drawLine(dX, 0, dX, myHeight + 1, myPaint);
			} else if (dX < 0 && dX > -myWidth) {
				canvas.drawLine(dX + myWidth, 0, dX + myWidth, myHeight + 1, myPaint);
			}
		} else {
			final int dY = myEndY - myStartY;
			drawBitmapTo(canvas, 0, dY > 0 ? dY - myHeight : dY + myHeight, myPaint);
			drawBitmapFrom(canvas, 0, dY, myPaint);
			if (dY > 0 && dY < myHeight) {
				canvas.drawLine(0, dY, myWidth + 1, dY, myPaint);
			} else if (dY < 0 && dY > -myHeight) {
				canvas.drawLine(0, dY + myHeight, myWidth + 1, dY + myHeight, myPaint);
			}
		}
	}

	@Override
	public void drawFooterBitmapInternal(Canvas canvas, Bitmap footerBitmap, int voffset) {
		canvas.drawBitmap(footerBitmap, 0, voffset, myPaint);
		if (myDirection.IsHorizontal) {
			final int dX = myEndX - myStartX;
			if (dX > 0 && dX < myWidth) {
				canvas.drawLine(dX, voffset, dX, voffset + footerBitmap.getHeight(), myPaint);
			} else if (dX < 0 && dX > -myWidth) {
				canvas.drawLine(dX + myWidth, voffset, dX + myWidth, voffset + footerBitmap.getHeight(), myPaint);
			}
		}
	}

	@Override
	protected void setFilter() {
		ViewUtil.setColorLevel(myPaint, myColorLevel);
	}
}
