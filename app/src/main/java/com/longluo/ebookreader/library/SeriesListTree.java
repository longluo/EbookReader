package com.longluo.ebookreader.library;

import java.util.Collections;

import com.longluo.ebookreader.book.*;

public class SeriesListTree extends FirstLevelTree {
	SeriesListTree(RootTree root) {
		super(root, ROOT_BY_SERIES);
	}

	@Override
	public Status getOpeningStatus() {
		if (!Collection.hasSeries()) {
			return Status.CANNOT_OPEN;
		}
		return Status.ALWAYS_RELOAD_BEFORE_OPENING;
	}

	@Override
	public String getOpeningStatusMessage() {
		return getOpeningStatus() == Status.CANNOT_OPEN
			? "noSeries" : super.getOpeningStatusMessage();
	}

	@Override
	public void waitForOpening() {
		clear();
		for (String s : Collection.series()) {
			createSeriesSubtree(s);
		}
	}

	@Override
	public boolean onBookEvent(BookEvent event, Book book) {
		switch (event) {
			case Added:
			case Updated:
			{
				// TODO: remove empty series tree after update (?)
				final SeriesInfo info = book.getSeriesInfo();
				// TODO: pass series
				return info != null && createSeriesSubtree(info.Series.getTitle());
			}
			case Removed:
				// TODO: remove empty series tree (?)
				return false;
			default:
				return false;
		}
	}

	private boolean createSeriesSubtree(String seriesTitle) {
		// TODO: pass series as parameter
		final Series series = new Series(seriesTitle);
		final SeriesTree temp = new SeriesTree(Collection, PluginCollection, series, null);
		int position = Collections.binarySearch(subtrees(), temp);
		if (position >= 0) {
			return false;
		} else {
			new SeriesTree(this, series, null, - position - 1);
			return true;
		}
	}
}
