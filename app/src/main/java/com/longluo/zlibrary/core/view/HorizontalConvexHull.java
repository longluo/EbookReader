package com.longluo.zlibrary.core.view;

import java.util.*;

import android.graphics.Rect;

public final class HorizontalConvexHull implements Hull {
	private final LinkedList<Rect> myRectangles = new LinkedList<Rect>();

	public HorizontalConvexHull(Collection<Rect> rects) {
		for (Rect r : rects) {
			addRect(r);
		}
		normalize();
	}

	private void addRect(Rect rectangle) {
		if (myRectangles.isEmpty()) {
			myRectangles.add(new Rect(rectangle));
			return;
		}
		final int top = rectangle.top;
		final int bottom = rectangle.bottom;
		for (ListIterator<Rect> iter = myRectangles.listIterator(); iter.hasNext(); ) {
			Rect r = iter.next();
			if (r.bottom <= top) {
				continue;
			}
			if (r.top >= bottom) {
				break;
			}
			if (r.top < top) {
				final Rect before = new Rect(r);
				before.bottom = top;
				r.top = top;
				iter.previous();
				iter.add(before);
				iter.next();
			}
			if (r.bottom > bottom) {
				final Rect after = new Rect(r);
				after.top = bottom;
				r.bottom = bottom;
				iter.add(after);
			}
			r.left = Math.min(r.left, rectangle.left);
			r.right = Math.max(r.right, rectangle.right);
		}

		final Rect first = myRectangles.getFirst();
		if (top < first.top) {
			myRectangles.add(0, new Rect(rectangle.left, top, rectangle.right, Math.min(bottom, first.top)));
		}

		final Rect last = myRectangles.getLast();
		if (bottom > last.bottom) {
			myRectangles.add(new Rect(rectangle.left, Math.max(top, last.bottom), rectangle.right, bottom));
		}
	}

	private void normalize() {
		Rect previous = null;
		for (ListIterator<Rect> iter = myRectangles.listIterator(); iter.hasNext(); ) {
			final Rect current = iter.next();
			if (previous != null) {
				if ((previous.left == current.left) && (previous.right == current.right)) {
					previous.bottom = current.bottom;
					iter.remove();
					continue;
				}
				if ((previous.bottom != current.top) &&
					(current.left <= previous.right) &&
					(previous.left <= current.right)) {
					iter.previous();
					iter.add(new Rect(
						Math.max(previous.left, current.left),
						previous.bottom,
						Math.min(previous.right, current.right),
						current.top
					));
					iter.next();
				}
			}
			previous = current;
		}
	}

	public int distanceTo(int x, int y) {
		int distance = Integer.MAX_VALUE;
		for (Rect r : myRectangles) {
			final int xd = r.left > x ? r.left - x : (r.right < x ? x - r.right : 0);
			final int yd = r.top > y ? r.top - y : (r.bottom < y ? y - r.bottom : 0);
			distance = Math.min(distance, Math.max(xd, yd));
			if (distance == 0) {
				break;
			}
		}
		return distance;
	}

	public boolean isBefore(int x, int y) {
		for (Rect r : myRectangles) {
			if (r.bottom < y || (r.top < y && r.right < x)) {
				return true;
			}
		}
		return false;
	}

	public void draw(ZLPaintContext context, int mode) {
		if (mode == DrawMode.None) {
			return;
		}

		final LinkedList<Rect> rectangles = new LinkedList<Rect>(myRectangles);
		while (!rectangles.isEmpty()) {
			final LinkedList<Rect> connected = new LinkedList<Rect>();
			Rect previous = null;
			for (final Iterator<Rect> iter = rectangles.iterator(); iter.hasNext(); ) {
				final Rect current = iter.next();
				if ((previous != null) &&
					((previous.left > current.right) || (current.left > previous.right))) {
					break;
				}
				iter.remove();
				connected.add(current);
				previous = current;
			}

			final LinkedList<Integer> xList = new LinkedList<Integer>();
			final LinkedList<Integer> yList = new LinkedList<Integer>();
			int x = 0, xPrev = 0;

			final ListIterator<Rect> iter = connected.listIterator();
			Rect r = iter.next();
			x = r.right + 2;
			xList.add(x); yList.add(r.top);
			while (iter.hasNext()) {
				xPrev = x;
				r = iter.next();
				x = r.right + 2;
				if (x != xPrev) {
					final int y = (x < xPrev) ? r.top + 2 : r.top;
					xList.add(xPrev); yList.add(y);
					xList.add(x); yList.add(y);
				}
			}
			xList.add(x); yList.add(r.bottom + 2);

			r = iter.previous();
			x = r.left - 2;
			xList.add(x); yList.add(r.bottom + 2);
			while (iter.hasPrevious()) {
				xPrev = x;
				r = iter.previous();
				x = r.left - 2;
				if (x != xPrev) {
					final int y = (x > xPrev) ? r.bottom : r.bottom + 2;
					xList.add(xPrev); yList.add(y);
					xList.add(x); yList.add(y);
				}
			}
			xList.add(x); yList.add(r.top);

			final int xs[] = new int[xList.size()];
			final int ys[] = new int[yList.size()];
			int count = 0;
			for (int xx : xList) {
				xs[count++] = xx;
			}
			count = 0;
			for (int yy : yList) {
				ys[count++] = yy;
			}

			if ((mode & DrawMode.Fill) == DrawMode.Fill) {
				context.fillPolygon(xs, ys);
			}
			if ((mode & DrawMode.Outline) == DrawMode.Outline) {
				context.drawOutline(xs, ys);
			}
		}
	}
}
