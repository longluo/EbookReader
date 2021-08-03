package com.longluo.ebookreader.book;

import java.util.List;

import com.longluo.zlibrary.text.view.ZLTextFixedPosition;
import com.longluo.zlibrary.text.view.ZLTextPosition;

public interface IBookCollection<B extends AbstractBook> extends AbstractSerializer.BookCreator<B> {
	public enum Status {
		NotStarted(false),
		Started(false),
		Succeeded(true),
		Failed(true);

		public final Boolean IsComplete;

		Status(boolean complete) {
			IsComplete = complete;
		}
	}

	public interface Listener<B> {
		void onBookEvent(BookEvent event, B book);
		void onBuildEvent(Status status);
	}

	public void addListener(Listener<B> listener);
	public void removeListener(Listener<B> listener);

	Status status();

	int size();

	List<B> books(BookQuery query);
	boolean hasBooks(Filter filter);
	List<String> titles(BookQuery query);

	List<B> recentlyOpenedBooks(int count);
	List<B> recentlyAddedBooks(int count);
	B getRecentBook(int index);
	void addToRecentlyOpened(B book);
	void removeFromRecentlyOpened(B book);

	B getBookByFile(String path);
	B getBookById(long id);
	B getBookByUid(UID uid);
	B getBookByHash(String hash);

	List<String> labels();
	List<Author> authors();
	boolean hasSeries();
	List<String> series();
	List<Tag> tags();
	List<String> firstTitleLetters();

	boolean saveBook(B book);
	boolean canRemoveBook(B book, boolean deleteFromDisk);
	void removeBook(B book, boolean deleteFromDisk);

	String getHash(B book, boolean force);
	void setHash(B book, String hash);
	boolean sameBook(B book0, B book1);

	ZLTextFixedPosition.WithTimestamp getStoredPosition(long bookId);
	void storePosition(long bookId, ZLTextPosition position);

	boolean isHyperlinkVisited(B book, String linkId);
	void markHyperlinkAsVisited(B book, String linkId);

	String getCoverUrl(B book);
	String getDescription(B book);

	List<Bookmark> bookmarks(BookmarkQuery query);
	void saveBookmark(Bookmark bookmark);
	void deleteBookmark(Bookmark bookmark);
	List<String> deletedBookmarkUids();
	void purgeBookmarks(List<String> uids);

	HighlightingStyle getHighlightingStyle(int styleId);
	List<HighlightingStyle> highlightingStyles();
	void saveHighlightingStyle(HighlightingStyle style);
	int getDefaultHighlightingStyleId();
	void setDefaultHighlightingStyleId(int styleId);

	class FormatDescriptor {
		public String Id;
		public String Name;
		public boolean IsActive;
	}
	List<FormatDescriptor> formats();
	// returns true iff active format set is changed
	boolean setActiveFormats(List<String> formatIds);

	void rescan(String path);
}
