package com.longluo.ebookreader.book;

public abstract class AbstractSerializer {
	public interface BookCreator<B extends AbstractBook> {
		B createBook(long id, String url, String title, String encoding, String language);
	}

	public abstract String serialize(BookQuery query);
	public abstract BookQuery deserializeBookQuery(String data);

	public abstract String serialize(BookmarkQuery query);
	public abstract BookmarkQuery deserializeBookmarkQuery(String data, BookCreator<? extends AbstractBook> creator);

	public abstract String serialize(AbstractBook book);
	public abstract <B extends AbstractBook> B deserializeBook(String data, BookCreator<B> creator);

	public abstract String serialize(Bookmark bookmark);
	public abstract Bookmark deserializeBookmark(String data);

	public abstract String serialize(HighlightingStyle style);
	public abstract HighlightingStyle deserializeStyle(String data);
}
