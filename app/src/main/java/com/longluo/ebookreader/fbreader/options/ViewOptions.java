package com.longluo.ebookreader.fbreader.options;

import com.longluo.zlibrary.core.library.ZLibrary;
import com.longluo.zlibrary.core.options.*;
import com.longluo.zlibrary.text.view.style.ZLTextStyleCollection;

import com.longluo.ebookreader.fbreader.FBView;

public class ViewOptions {
	public final ZLBooleanOption TwoColumnView;
	public final ZLIntegerRangeOption LeftMargin;
	public final ZLIntegerRangeOption RightMargin;
	public final ZLIntegerRangeOption TopMargin;
	public final ZLIntegerRangeOption BottomMargin;
	public final ZLIntegerRangeOption SpaceBetweenColumns;
	public final ZLIntegerRangeOption ScrollbarType;
	public final ZLIntegerRangeOption FooterHeight;
	public final ZLStringOption ColorProfileName;

	private ColorProfile myColorProfile;
	private ZLTextStyleCollection myTextStyleCollection;
	private FooterOptions myFooterOptions;

	public ViewOptions() {
		final ZLibrary zlibrary = ZLibrary.Instance();

		final int dpi = zlibrary.getDisplayDPI();
		final int x = zlibrary.getWidthInPixels();
		final int y = zlibrary.getHeightInPixels();
		final int horMargin = Math.min(dpi / 5, Math.min(x, y) / 30);

		TwoColumnView =
			new ZLBooleanOption("Options", "TwoColumnView", x * x + y * y >= 42 * dpi * dpi);
		LeftMargin =
			new ZLIntegerRangeOption("Options", "LeftMargin", 0, 100, horMargin);
		RightMargin =
			new ZLIntegerRangeOption("Options", "RightMargin", 0, 100, horMargin);
		TopMargin =
			new ZLIntegerRangeOption("Options", "TopMargin", 0, 100, 0);
		BottomMargin =
			new ZLIntegerRangeOption("Options", "BottomMargin", 0, 100, 4);
		SpaceBetweenColumns =
			new ZLIntegerRangeOption("Options", "SpaceBetweenColumns", 0, 300, 3 * horMargin);
		ScrollbarType =
			new ZLIntegerRangeOption("Options", "ScrollbarType", 0, 4, FBView.SCROLLBAR_SHOW_AS_FOOTER);
		FooterHeight =
			new ZLIntegerRangeOption("Options", "FooterHeight", 8, dpi / 8, dpi / 20);
		ColorProfileName =
			new ZLStringOption("Options", "ColorProfile", ColorProfile.DAY);
		ColorProfileName.setSpecialName("colorProfile");
	}

	public ColorProfile getColorProfile() {
		final String name = ColorProfileName.getValue();
		if (myColorProfile == null || !name.equals(myColorProfile.Name)) {
			myColorProfile = ColorProfile.get(name);
		}
		return myColorProfile;
	}

	public ZLTextStyleCollection getTextStyleCollection() {
		if (myTextStyleCollection == null) {
			myTextStyleCollection = new ZLTextStyleCollection("Base");
		}
		return myTextStyleCollection;
	}

	public FooterOptions getFooterOptions() {
		if (myFooterOptions == null) {
			myFooterOptions = new FooterOptions();
		}
		return myFooterOptions;
	}
}
