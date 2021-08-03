package com.longluo.zlibrary.text.view;

import java.util.List;

import com.longluo.zlibrary.core.util.ZLColor;
import com.longluo.zlibrary.core.view.Hull;

public abstract class ZLTextHighlighting implements Comparable<ZLTextHighlighting> {
	public abstract boolean isEmpty();

	public abstract ZLTextPosition getStartPosition();
	public abstract ZLTextPosition getEndPosition();
	public abstract ZLTextElementArea getStartArea(ZLTextPage page);
	public abstract ZLTextElementArea getEndArea(ZLTextPage page);

	public abstract ZLColor getForegroundColor();
	public abstract ZLColor getBackgroundColor();
	public abstract ZLColor getOutlineColor();

	boolean intersects(ZLTextPage page) {
		return
			!isEmpty() &&
			!page.StartCursor.isNull() && !page.EndCursor.isNull() &&
			page.StartCursor.compareTo(getEndPosition()) < 0 &&
			page.EndCursor.compareTo(getStartPosition()) > 0;
	}

	boolean intersects(ZLTextRegion region) {
		final ZLTextRegion.Soul soul = region.getSoul();
		return
			!isEmpty() &&
			soul.compareTo(getStartPosition()) >= 0 &&
			soul.compareTo(getEndPosition()) <= 0;
	}

	final Hull hull(ZLTextPage page) {
		final ZLTextPosition startPosition = getStartPosition();
		final ZLTextPosition endPosition = getEndPosition();
		final List<ZLTextElementArea> areas = page.TextElementMap.areas();
		int startIndex = 0;
		int endIndex = 0;
		for (int i = 0; i < areas.size(); ++i) {
			final ZLTextElementArea a = areas.get(i);
			if (i == startIndex && startPosition.compareTo(a) > 0) {
				++startIndex;
			} else if (endPosition.compareTo(a) < 0) {
				break;
			}
			++endIndex;
		}
		return HullUtil.hull(areas.subList(startIndex, endIndex));
	}

	public int compareTo(ZLTextHighlighting highlighting) {
		final int cmp = getStartPosition().compareTo(highlighting.getStartPosition());
		return cmp != 0 ? cmp : getEndPosition().compareTo(highlighting.getEndPosition());
	}
}
