package com.longluo.ebookreader.book;

import java.util.*;

public abstract class AbstractBookCollection<B extends AbstractBook> implements IBookCollection<B> {
	private final List<Listener> myListeners = Collections.synchronizedList(new LinkedList<Listener>());

	public void addListener(Listener listener) {
		if (!myListeners.contains(listener)) {
			myListeners.add(listener);
		}
	}

	public void removeListener(Listener listener) {
		myListeners.remove(listener);
	}

	protected boolean hasListeners() {
		return !myListeners.isEmpty();
	}

	protected void fireBookEvent(BookEvent event, B book) {
		synchronized (myListeners) {
			for (Listener l : myListeners) {
				l.onBookEvent(event, book);
			}
		}
	}

	protected void fireBuildEvent(Status status) {
		synchronized (myListeners) {
			for (Listener l : myListeners) {
				l.onBuildEvent(status);
			}
		}
	}

	public boolean sameBook(B b0, B b1) {
		if (b0 == b1) {
			return true;
		}
		if (b0 == null || b1 == null) {
			return false;
		}

		if (b0.getPath().equals(b1.getPath())) {
			return true;
		}

		final String hash0 = getHash(b0, false);
		return hash0 != null && hash0.equals(getHash(b1, false));
	}
}
