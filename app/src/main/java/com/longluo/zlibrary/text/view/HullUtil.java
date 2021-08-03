package com.longluo.zlibrary.text.view;

import java.util.*;

import android.graphics.Rect;

import com.longluo.zlibrary.core.view.*;

abstract class HullUtil {
	static Hull hull(ZLTextElementArea[] areas) {
		return hull(Arrays.asList(areas));
	}

	static Hull hull(List<ZLTextElementArea> areas) {
		final List<Rect> rectangles0 = new ArrayList<Rect>(areas.size());
		final List<Rect> rectangles1 = new ArrayList<Rect>(areas.size());
		for (ZLTextElementArea a : areas) {
			final Rect rect = new Rect(a.XStart, a.YStart, a.XEnd, a.YEnd);
			if (a.ColumnIndex == 0) {
				rectangles0.add(rect);
			} else {
				rectangles1.add(rect);
			}
		}
		if (rectangles0.isEmpty()) {
			return new HorizontalConvexHull(rectangles1);
		} else if (rectangles1.isEmpty()) {
			return new HorizontalConvexHull(rectangles0);
		} else {
			return new UnionHull(
				new HorizontalConvexHull(rectangles0),
				new HorizontalConvexHull(rectangles1)
			);
		}
	}
}
