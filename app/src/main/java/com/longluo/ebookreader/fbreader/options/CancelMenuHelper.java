package com.longluo.ebookreader.fbreader.options;

import java.util.*;

import com.longluo.zlibrary.core.options.Config;
import com.longluo.zlibrary.core.options.ZLBooleanOption;
import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.book.*;

public class CancelMenuHelper {
	private final static String GROUP_NAME = "CancelMenu";

	public final ZLBooleanOption ShowLibraryItemOption =
		new ZLBooleanOption(GROUP_NAME, "library", true);
	public final ZLBooleanOption ShowNetworkLibraryItemOption =
		new ZLBooleanOption(GROUP_NAME, "networkLibrary", true);
	public final ZLBooleanOption ShowPreviousBookItemOption =
		new ZLBooleanOption(GROUP_NAME, "previousBook", false);
	public final ZLBooleanOption ShowPositionItemsOption =
		new ZLBooleanOption(GROUP_NAME, "positions", true);

	public CancelMenuHelper() {
		Config.Instance().requestAllValuesForGroup(GROUP_NAME);
	}

	public static enum ActionType {
		library,
		networkLibrary,
		previousBook,
		returnTo,
		close
	}

	public static class ActionDescription {
		public final ActionType Type;
		public final String Title;
		public final String Summary;

		ActionDescription(ActionType type, String summary) {
			final ZLResource resource = ZLResource.resource("cancelMenu");
			Type = type;
			Title = resource.getResource(type.toString()).getValue();
			Summary = summary;
		}
	}

	public static class BookmarkDescription extends ActionDescription {
		public final Bookmark Bookmark;

		BookmarkDescription(Bookmark b) {
			super(ActionType.returnTo, b.getText());
			Bookmark = b;
		}
	}

	public List<ActionDescription> getActionsList(IBookCollection<Book> collection) {
		final List<ActionDescription> list = new ArrayList<ActionDescription>();

		if (ShowLibraryItemOption.getValue()) {
			list.add(new ActionDescription(ActionType.library, null));
		}
		if (ShowNetworkLibraryItemOption.getValue()) {
			list.add(new ActionDescription(ActionType.networkLibrary, null));
		}
		if (ShowPreviousBookItemOption.getValue()) {
			final Book previousBook = collection.getRecentBook(1);
			if (previousBook != null) {
				list.add(new ActionDescription(ActionType.previousBook, previousBook.getTitle()));
			}
		}
		if (ShowPositionItemsOption.getValue()) {
			final Book currentBook = collection.getRecentBook(0);
			if (currentBook != null) {
				final List<Bookmark> bookmarks = collection.bookmarks(
					new BookmarkQuery(currentBook, false, 3)
				);
				Collections.sort(bookmarks, new Bookmark.ByTimeComparator());
				for (Bookmark b : bookmarks) {
					list.add(new BookmarkDescription(b));
				}
			}
		}
		list.add(new ActionDescription(ActionType.close, null));

		return list;
	}
}
