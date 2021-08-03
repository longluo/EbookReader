package com.longluo.ebookreader.book;

import java.util.*;

public abstract class SerializerUtil {
	private SerializerUtil() {
	}

	private static final AbstractSerializer defaultSerializer = new XMLSerializer();

	public static String serialize(BookQuery query) {
		return query != null ? defaultSerializer.serialize(query) : null;
	}

	public static BookQuery deserializeBookQuery(String xml) {
		return xml != null ? defaultSerializer.deserializeBookQuery(xml) : null;
	}

	public static String serialize(BookmarkQuery query) {
		return query != null ? defaultSerializer.serialize(query) : null;
	}

	public static BookmarkQuery deserializeBookmarkQuery(String xml, AbstractSerializer.BookCreator<? extends AbstractBook> creator) {
		return xml != null ? defaultSerializer.deserializeBookmarkQuery(xml, creator) : null;
	}

	public static String serialize(AbstractBook book) {
		return book != null ? defaultSerializer.serialize(book) : null;
	}

	public static <B extends AbstractBook> B deserializeBook(String xml, AbstractSerializer.BookCreator<B> creator) {
		return xml != null ? defaultSerializer.deserializeBook(xml, creator) : null;
	}

	public static List<String> serializeBookList(List<? extends AbstractBook> books) {
		final List<String> serialized = new ArrayList<String>(books.size());
		for (AbstractBook b : books) {
			serialized.add(defaultSerializer.serialize(b));
		}
		return serialized;
	}

	public static <B extends AbstractBook> List<B> deserializeBookList(List<String> xmlList, AbstractSerializer.BookCreator<B> creator) {
		final List<B> books = new ArrayList<B>(xmlList.size());
		for (String xml : xmlList) {
			final B b = defaultSerializer.deserializeBook(xml, creator);
			if (b != null) {
				books.add(b);
			}
		}
		return books;
	}

	public static String serialize(Bookmark bookmark) {
		return bookmark != null ? defaultSerializer.serialize(bookmark) : null;
	}

	public static Bookmark deserializeBookmark(String xml) {
		return xml != null ? defaultSerializer.deserializeBookmark(xml) : null;
	}

	public static List<String> serializeBookmarkList(List<Bookmark> bookmarks) {
		final List<String> serialized = new ArrayList<String>(bookmarks.size());
		for (Bookmark b : bookmarks) {
			serialized.add(defaultSerializer.serialize(b));
		}
		return serialized;
	}

	public static List<Bookmark> deserializeBookmarkList(List<String> xmlList) {
		final List<Bookmark> bookmarks = new ArrayList<Bookmark>(xmlList.size());
		for (String xml : xmlList) {
			final Bookmark b = defaultSerializer.deserializeBookmark(xml);
			if (b != null) {
				bookmarks.add(b);
			}
		}
		return bookmarks;
	}

	public static String serialize(HighlightingStyle style) {
		return style != null ? defaultSerializer.serialize(style) : null;
	}

	public static HighlightingStyle deserializeStyle(String xml) {
		return xml != null ? defaultSerializer.deserializeStyle(xml) : null;
	}

	public static List<String> serializeStyleList(List<HighlightingStyle> styles) {
		final List<String> serialized = new ArrayList<String>(styles.size());
		for (HighlightingStyle s : styles) {
			serialized.add(defaultSerializer.serialize(s));
		}
		return serialized;
	}

	public static List<HighlightingStyle> deserializeStyleList(List<String> xmlList) {
		final List<HighlightingStyle> styles = new ArrayList<HighlightingStyle>(xmlList.size());
		for (String xml : xmlList) {
			final HighlightingStyle s = defaultSerializer.deserializeStyle(xml);
			if (s != null) {
				styles.add(s);
			}
		}
		return styles;
	}
}
