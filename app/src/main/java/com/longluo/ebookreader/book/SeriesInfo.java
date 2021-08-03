package com.longluo.ebookreader.book;

import java.math.BigDecimal;

import com.longluo.util.ComparisonUtil;

public final class SeriesInfo implements Comparable<SeriesInfo> {
	public static SeriesInfo createSeriesInfo(String title, String index) {
		if (title == null) {
			return null;
		}
		return new SeriesInfo(title, createIndex(index));
	}

	public static BigDecimal createIndex(String index) {
		try {
			return index != null ? new BigDecimal(index).stripTrailingZeros() : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public final Series Series;
	public final BigDecimal Index;

	SeriesInfo(String title, BigDecimal index) {
		Series = new Series(title);
		Index = index;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SeriesInfo)) {
			return false;
		}
		final SeriesInfo info = (SeriesInfo)other;
		return ComparisonUtil.equal(Series, info.Series) && ComparisonUtil.equal(Index, info.Index);
	}

	@Override
	public int hashCode() {
		return 23 * ComparisonUtil.hashCode(Series) + 31 * ComparisonUtil.hashCode(Index);
	}

	@Override
	public int compareTo(SeriesInfo other) {
		final BigDecimal i0 = Index != null ? Index : BigDecimal.ZERO;
		final BigDecimal i1 = other.Index != null ? other.Index : BigDecimal.ZERO;
		return i0.compareTo(i1);
	}
}
