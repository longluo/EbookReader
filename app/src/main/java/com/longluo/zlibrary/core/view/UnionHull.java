package com.longluo.zlibrary.core.view;

import java.util.*;

public class UnionHull implements Hull {
	private final List<Hull> myComponents;

	public UnionHull(Hull ... components) {
		myComponents = new ArrayList<Hull>(Arrays.asList(components));
	}

	public void draw(ZLPaintContext context, int mode) {
		for (Hull h : myComponents) {
			h.draw(context, mode);
		}
	}

	public int distanceTo(int x, int y) {
		int dist = Integer.MAX_VALUE;
		for (Hull h : myComponents) {
			dist = Math.min(dist, h.distanceTo(x, y));
		}
		return dist;
	}

	public boolean isBefore(int x, int y) {
		for (Hull h : myComponents) {
			if (h.isBefore(x, y)) {
				return true;
			}
		}
		return false;
	}
}
