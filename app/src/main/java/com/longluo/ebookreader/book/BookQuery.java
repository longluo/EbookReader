package com.longluo.ebookreader.book;

public final class BookQuery {
	public final Filter Filter;
	public final int Limit;
	public final int Page;

	public BookQuery(Filter filter, int limit) {
		this(filter, limit, 0);
	}

	BookQuery(Filter filter, int limit, int page) {
		Filter = filter;
		Limit = limit;
		Page = page;
	}

	public BookQuery next() {
		return new BookQuery(Filter, Limit, Page + 1);
	}
}
