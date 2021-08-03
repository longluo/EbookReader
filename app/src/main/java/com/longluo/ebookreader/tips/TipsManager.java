package com.longluo.ebookreader.tips;

import java.util.*;
import java.io.File;

import com.longluo.zlibrary.core.filesystem.ZLFile;
import com.longluo.zlibrary.core.options.*;
import com.longluo.zlibrary.core.network.QuietNetworkContext;
import com.longluo.zlibrary.core.network.ZLNetworkException;
import com.longluo.zlibrary.core.util.SystemInfo;

import com.longluo.ebookreader.network.NetworkLibrary;
import com.longluo.ebookreader.network.atom.ATOMXMLReader;

public class TipsManager {
	private final SystemInfo mySystemInfo;

	public static final ZLBooleanOption TipsAreInitializedOption =
		new ZLBooleanOption("tips", "tipsAreInitialized", false);
	public static final ZLBooleanOption ShowTipsOption =
		new ZLBooleanOption("tips", "showTips", false);

	// time when last tip was shown, 2^16 milliseconds
	private final ZLIntegerOption myLastShownOption;
	// index of next tip to show
	private final ZLIntegerOption myIndexOption;

	private volatile boolean myDownloadInProgress;

	public TipsManager(SystemInfo systemInfo) {
		mySystemInfo = systemInfo;

		myLastShownOption = new ZLIntegerOption("tips", "shownAt", 0);
		myIndexOption = new ZLIntegerOption("tips", "index", 0);
	}

	private String getUrl() {
		return "https://data.fbreader.org/tips/tips.php";
	}

	private String getLocalFilePath() {
		return mySystemInfo.networkCacheDirectory() + "/tips/tips.xml";
	}

	private List<Tip> myTips;
	private List<Tip> getTips() {
		if (myTips == null) {
			final ZLFile file = ZLFile.createFileByPath(getLocalFilePath());
			if (file.exists()) {
				final TipsFeedHandler handler = new TipsFeedHandler();
				new ATOMXMLReader(NetworkLibrary.Instance(mySystemInfo), handler, false).readQuietly(file);
				final List<Tip> tips = Collections.unmodifiableList(handler.Tips);
				if (tips.size() > 0) {
					myTips = tips;
				}
			}
		}
		return myTips;
	}

	public boolean hasNextTip() {
		final List<Tip> tips = getTips();
		if (tips == null) {
			return false;
		}

		final int index = myIndexOption.getValue();
		if (index >= tips.size()) {
			new File(getLocalFilePath()).delete();
			myIndexOption.setValue(0);
			return false;
		}

		return true;
	}

	public Tip getNextTip() {
		final List<Tip> tips = getTips();
		if (tips == null) {
			return null;
		}

		final int index = myIndexOption.getValue();
		if (index >= tips.size()) {
			new File(getLocalFilePath()).delete();
			myIndexOption.setValue(0);
			return null;
		}

		myIndexOption.setValue(index + 1);
		myLastShownOption.setValue(currentTime());
		return tips.get(index);
	}

	private final int DELAY = (24 * 60 * 60 * 1000) >> 16; // 1 day

	private int currentTime() {
		return (int)(new Date().getTime() >> 16);
	}

	public static enum Action {
		Initialize,
		Show,
		Download,
		None
	}

	public Action requiredAction() {
		if (ShowTipsOption.getValue()) {
			if (hasNextTip()) {
				return myLastShownOption.getValue() + DELAY < currentTime()
					? Action.Show : Action.None;
			} else {
				return myDownloadInProgress
					? Action.None : Action.Download;
			}
		} else if (!TipsAreInitializedOption.getValue()) {
			//return Action.Initialize;
			return Action.None;
		}
		return Action.None;
	}

	public synchronized void startDownloading() {
		if (requiredAction() != Action.Download) {
			return;
		}

		myDownloadInProgress = true;

		Config.Instance().runOnConnect(new Runnable() {
			public void run() {
				final File tipsFile = new File(getLocalFilePath());
				tipsFile.getParentFile().mkdirs();
				new Thread(new Runnable() {
					public void run() {
						new QuietNetworkContext().downloadToFileQuietly(getUrl(), tipsFile);
						myDownloadInProgress = false;
					}
				}).start();
			}
		});
	}
}
