package com.longluo.ebookreader.book;

import com.longluo.ebookreader.sort.TitledEntity;

public class Series extends TitledEntity {
	public Series(String title) {
		super(title);
	}

	public String getLanguage() {
		// TODO: return real language
		return "en";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Series)) {
			return false;
		}
		return getTitle().equals(((Series)o).getTitle());
	}

	@Override
	public int hashCode() {
		return getTitle().hashCode();
	}
}
