package com.longluo.zlibrary.text.view;

import java.util.List;

import com.longluo.zlibrary.core.fonts.FontEntry;
import com.longluo.zlibrary.text.model.ZLTextMetrics;

public abstract class ZLTextStyle {
	public final ZLTextStyle Parent;
	public final ZLTextHyperlink Hyperlink;

	protected ZLTextStyle(ZLTextStyle parent, ZLTextHyperlink hyperlink) {
		Parent = parent != null ? parent : this;
		Hyperlink = hyperlink;
	}

	public abstract List<FontEntry> getFontEntries();
	public abstract int getFontSize(ZLTextMetrics metrics);

	public abstract boolean isBold();
	public abstract boolean isItalic();
	public abstract boolean isUnderline();
	public abstract boolean isStrikeThrough();

	public final int getLeftIndent(ZLTextMetrics metrics) {
		return getLeftMargin(metrics) + getLeftPadding(metrics);
	}
	public final int getRightIndent(ZLTextMetrics metrics) {
		return getRightMargin(metrics) + getRightPadding(metrics);
	}
	public abstract int getLeftMargin(ZLTextMetrics metrics);
	public abstract int getRightMargin(ZLTextMetrics metrics);
	public abstract int getLeftPadding(ZLTextMetrics metrics);
	public abstract int getRightPadding(ZLTextMetrics metrics);

	public abstract int getFirstLineIndent(ZLTextMetrics metrics);
	public abstract int getLineSpacePercent();
	public abstract int getVerticalAlign(ZLTextMetrics metrics);
	public abstract boolean isVerticallyAligned();
	public abstract int getSpaceBefore(ZLTextMetrics metrics);
	public abstract int getSpaceAfter(ZLTextMetrics metrics);
	public abstract byte getAlignment();

	public abstract boolean allowHyphenations();
}
