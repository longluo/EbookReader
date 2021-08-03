package com.longluo.zlibrary.core.view;

import com.longluo.zlibrary.core.application.ZLApplication;

abstract public class ZLView implements ZLViewEnums {
	public final ZLApplication Application;
	private ZLPaintContext myViewContext = new DummyPaintContext();

	protected ZLView(ZLApplication application) {
		Application = application;
	}

	protected final void setContext(ZLPaintContext context) {
		myViewContext = context;
	}

	public final ZLPaintContext getContext() {
		return myViewContext;
	}

	public final int getContextWidth() {
		return myViewContext.getWidth();
	}

	public final int getContextHeight() {
		return myViewContext.getHeight();
	}

	abstract public interface FooterArea {
		int getHeight();
		void paint(ZLPaintContext context);
	}

	abstract public FooterArea getFooterArea();

	public abstract Animation getAnimationType();

	abstract public void preparePage(ZLPaintContext context, PageIndex pageIndex);
	abstract public void paint(ZLPaintContext context, PageIndex pageIndex);
	abstract public void onScrollingFinished(PageIndex pageIndex);

	public abstract void onFingerPress(int x, int y);
	public abstract void onFingerRelease(int x, int y);
	public abstract void onFingerMove(int x, int y);
	public abstract boolean onFingerLongPress(int x, int y);
	public abstract void onFingerReleaseAfterLongPress(int x, int y);
	public abstract void onFingerMoveAfterLongPress(int x, int y);
	public abstract void onFingerSingleTap(int x, int y);
	public abstract void onFingerDoubleTap(int x, int y);
	public abstract void onFingerEventCancelled();

	public boolean isDoubleTapSupported() {
		return false;
	}

	public boolean onTrackballRotated(int diffX, int diffY) {
		return false;
	}

	public abstract boolean isScrollbarShown();
	public abstract int getScrollbarFullSize();
	public abstract int getScrollbarThumbPosition(PageIndex pageIndex);
	public abstract int getScrollbarThumbLength(PageIndex pageIndex);

	public abstract boolean canScroll(PageIndex index);
}
