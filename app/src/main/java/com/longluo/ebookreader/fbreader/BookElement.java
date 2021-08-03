package com.longluo.ebookreader.fbreader;

import com.longluo.zlibrary.core.image.ZLImageData;
import com.longluo.zlibrary.core.image.ZLImageManager;
import com.longluo.zlibrary.core.library.ZLibrary;
import com.longluo.zlibrary.core.util.ZLColor;
import com.longluo.zlibrary.core.view.ZLPaintContext;

import com.longluo.zlibrary.text.view.*;

import com.longluo.ebookreader.network.NetworkImage;
import com.longluo.ebookreader.network.opds.OPDSBookItem;
import com.longluo.ebookreader.network.urlInfo.UrlInfo;

public final class BookElement extends ExtensionElement {
	private final FBView myView;

	private OPDSBookItem myItem;
	private NetworkImage myCover;

	BookElement(FBView view) {
		myView = view;
	}

	public void setData(OPDSBookItem item) {
		final String bookUrl = item.getUrl(UrlInfo.Type.Book);
		String coverUrl = item.getUrl(UrlInfo.Type.Image);
		if (coverUrl == null) {
			coverUrl = item.getUrl(UrlInfo.Type.Thumbnail);
		}
		if (bookUrl == null || coverUrl == null) {
			myItem = null;
			myCover = null;
		} else {
			myItem = item;
			myCover = new NetworkImage(coverUrl, myView.Application.SystemInfo);
			myCover.synchronize();
		}
	}

	public boolean isInitialized() {
		return myItem != null && myCover != null;
	}

	public OPDSBookItem getItem() {
		return myItem;
	}

	public ZLImageData getImageData() {
		if (myCover == null) {
			return null;
		}
		return ZLImageManager.Instance().getImageData(myCover);
	}

	@Override
	protected int getWidth() {
		// 1/\phi (= 0.618) inch width + 1/10 inch left & right margin
		return Math.min(ZLibrary.Instance().getDisplayDPI() * 818 / 1000, myView.getTextColumnWidth());
	}

	@Override
	protected int getHeight() {
		// 1 inch height + 1/15 inch top & bottom margin
		return ZLibrary.Instance().getDisplayDPI() * 17 / 15;
	}

	@Override
	protected void draw(ZLPaintContext context, ZLTextElementArea area) {
		final int vMargin = ZLibrary.Instance().getDisplayDPI() / 15;
		final int hMargin = ZLibrary.Instance().getDisplayDPI() / 10;
		final ZLImageData imageData = getImageData();
		if (imageData != null) {
			context.drawImage(
				area.XStart + hMargin, area.YEnd - vMargin,
				imageData,
				new ZLPaintContext.Size(
					area.XEnd - area.XStart - 2 * hMargin + 1,
					area.YEnd - area.YStart - 2 * vMargin + 1
				),
				ZLPaintContext.ScalingType.FitMaximum,
				ZLPaintContext.ColorAdjustingMode.NONE
			);
		} else {
			final ZLColor color = myView.getTextColor(ZLTextHyperlink.NO_LINK);
			context.setLineColor(color);
			context.setFillColor(color, 0x33);
			final int xStart = area.XStart + hMargin;
			final int xEnd = area.XEnd - hMargin;
			final int yStart = area.YStart + vMargin;
			final int yEnd = area.YEnd - vMargin;
			context.fillRectangle(xStart, yStart, xEnd, yEnd);
			context.drawLine(xStart, yStart, xStart, yEnd);
			context.drawLine(xStart, yEnd, xEnd, yEnd);
			context.drawLine(xEnd, yEnd, xEnd, yStart);
			context.drawLine(xEnd, yStart, xStart, yStart);
		}
	}
}
