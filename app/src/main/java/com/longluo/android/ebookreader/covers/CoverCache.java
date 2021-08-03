package com.longluo.android.ebookreader.covers;

import java.util.*;

import android.graphics.Bitmap;

import com.longluo.ebookreader.tree.FBTree;

class CoverCache {
	static class NullObjectException extends Exception {
	}

	private static final Object NULL_BITMAP = new Object();

	volatile int HoldersCounter = 0;

	private final Map<FBTree.Key,Object> myBitmaps =
		Collections.synchronizedMap(new LinkedHashMap<FBTree.Key,Object>(10, 0.75f, true) {
			@Override
			protected boolean removeEldestEntry(Map.Entry<FBTree.Key,Object> eldest) {
				return size() > 3 * HoldersCounter;
			}
		});

	Bitmap getBitmap(FBTree.Key key) throws NullObjectException {
		final Object bitmap = myBitmaps.get(key);
		if (bitmap == NULL_BITMAP) {
			throw new NullObjectException();
		}
		return (Bitmap)bitmap;
	}

	void putBitmap(FBTree.Key key, Bitmap bitmap) {
		myBitmaps.put(key, bitmap != null ? bitmap : NULL_BITMAP);
	}
}
