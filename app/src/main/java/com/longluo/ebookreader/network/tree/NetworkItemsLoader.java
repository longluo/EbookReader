package com.longluo.ebookreader.network.tree;

import com.longluo.zlibrary.core.network.ZLNetworkContext;
import com.longluo.zlibrary.core.network.ZLNetworkException;

import com.longluo.ebookreader.network.NetworkLibrary;
import com.longluo.ebookreader.network.NetworkItem;

public abstract class NetworkItemsLoader implements Runnable {
	private volatile Runnable myPostRunnable;
	private volatile boolean myFinishedFlag;
	public final ZLNetworkContext NetworkContext;
	public final NetworkCatalogTree Tree;

	protected NetworkItemsLoader(ZLNetworkContext nc, NetworkCatalogTree tree) {
		NetworkContext = nc;
		Tree = tree;
	}

	public final void start() {
		final Thread loaderThread = new Thread(this);
		loaderThread.setPriority(Thread.MIN_PRIORITY);
		loaderThread.start();
	}

	public final void run() {
		final NetworkLibrary library = Tree.Library;

		synchronized (library) {
			if (library.isLoadingInProgress(Tree)) {
				return;
			}
			library.storeLoader(Tree, this);
		}

		try {
			library.fireModelChangedEvent(NetworkLibrary.ChangeListener.Code.SomeCode);

			try {
				doBefore();
			} catch (ZLNetworkException e) {
				onFinish(e, false);
				return;
			}

			try {
				load();
				onFinish(null, isLoadingInterrupted());
			} catch (ZLNetworkException e) {
				onFinish(e, isLoadingInterrupted());
			}
		} finally {
			library.removeStoredLoader(Tree);
			library.fireModelChangedEvent(NetworkLibrary.ChangeListener.Code.SomeCode);
			synchronized (this) {
				if (myPostRunnable != null) {
					myPostRunnable.run();
					myFinishedFlag = true;
				}
			}
		}
	}

	private final Object myInterruptLock = new Object();
	private enum InterruptionState {
		NONE,
		REQUESTED,
		CONFIRMED
	};
	private InterruptionState myInterruptionState = InterruptionState.NONE;

	public final boolean canResumeLoading() {
		synchronized (myInterruptLock) {
			if (myInterruptionState == InterruptionState.REQUESTED) {
				myInterruptionState = InterruptionState.NONE;
			}
			return myInterruptionState == InterruptionState.NONE;
		}
	}

	protected final boolean isLoadingInterrupted() {
		synchronized (myInterruptLock) {
			return myInterruptionState == InterruptionState.CONFIRMED;
		}
	}

	public void interrupt() {
		synchronized (myInterruptLock) {
			if (myInterruptionState == InterruptionState.NONE) {
				myInterruptionState = InterruptionState.REQUESTED;
			}
		}
	}

	public final boolean confirmInterruption() {
		synchronized (myInterruptLock) {
			if (myInterruptionState == InterruptionState.REQUESTED) {
				myInterruptionState = InterruptionState.CONFIRMED;
			}
			return myInterruptionState == InterruptionState.CONFIRMED;
		}
	}

	public void onNewItem(final NetworkItem item) {
		Tree.addItem(item);
	}

	public synchronized void setPostRunnable(Runnable action) {
		if (myFinishedFlag) {
			action.run();
		} else {
			myPostRunnable = action;
		}
	}

	protected abstract void onFinish(ZLNetworkException exception, boolean interrupted);
	protected abstract void doBefore() throws ZLNetworkException;
	protected abstract void load() throws ZLNetworkException;
}
