package com.longluo.android.ebookreader;

import java.util.*;

import com.longluo.zlibrary.core.library.ZLibrary;
import com.longluo.ebookreader.R;

import com.longluo.ebookreader.fbreader.ActionCode;

import com.longluo.android.ebookreader.api.MenuNode;

public abstract class MenuData {
	private static List<MenuNode> ourNodes;

	private static void addToplevelNode(MenuNode node) {
		ourNodes.add(node);
	}

	public static synchronized List<MenuNode> topLevelNodes() {
		if (ourNodes == null) {
			ourNodes = new ArrayList<MenuNode>();
			addToplevelNode(new MenuNode.Item(ActionCode.SHOW_LIBRARY, R.drawable.ic_menu_library));
			addToplevelNode(new MenuNode.Item(ActionCode.SHOW_NETWORK_LIBRARY, R.drawable.ic_menu_networklibrary));
			addToplevelNode(new MenuNode.Item(ActionCode.SHOW_TOC, R.drawable.ic_menu_toc));
			addToplevelNode(new MenuNode.Item(ActionCode.SHOW_BOOKMARKS, R.drawable.ic_menu_bookmarks));
			addToplevelNode(new MenuNode.Item(ActionCode.SWITCH_TO_NIGHT_PROFILE, R.drawable.ic_menu_night));
			addToplevelNode(new MenuNode.Item(ActionCode.SWITCH_TO_DAY_PROFILE, R.drawable.ic_menu_day));
			addToplevelNode(new MenuNode.Item(ActionCode.SEARCH, R.drawable.ic_menu_search));
			addToplevelNode(new MenuNode.Item(ActionCode.SHARE_BOOK));
			addToplevelNode(new MenuNode.Item(ActionCode.SHOW_PREFERENCES));
			addToplevelNode(new MenuNode.Item(ActionCode.SHOW_BOOK_INFO));
			final MenuNode.Submenu orientations = new MenuNode.Submenu("screenOrientation");
			orientations.Children.add(new MenuNode.Item(ActionCode.SET_SCREEN_ORIENTATION_SYSTEM));
			orientations.Children.add(new MenuNode.Item(ActionCode.SET_SCREEN_ORIENTATION_SENSOR));
			orientations.Children.add(new MenuNode.Item(ActionCode.SET_SCREEN_ORIENTATION_PORTRAIT));
			orientations.Children.add(new MenuNode.Item(ActionCode.SET_SCREEN_ORIENTATION_LANDSCAPE));
			if (ZLibrary.Instance().supportsAllOrientations()) {
				orientations.Children.add(new MenuNode.Item(ActionCode.SET_SCREEN_ORIENTATION_REVERSE_PORTRAIT));
				orientations.Children.add(new MenuNode.Item(ActionCode.SET_SCREEN_ORIENTATION_REVERSE_LANDSCAPE));
			}
			addToplevelNode(orientations);
			addToplevelNode(new MenuNode.Item(ActionCode.INCREASE_FONT));
			addToplevelNode(new MenuNode.Item(ActionCode.DECREASE_FONT));
			addToplevelNode(new MenuNode.Item(ActionCode.SHOW_NAVIGATION));
			addToplevelNode(new MenuNode.Item(ActionCode.INSTALL_PLUGINS));
			addToplevelNode(new MenuNode.Item(ActionCode.OPEN_WEB_HELP));
			addToplevelNode(new MenuNode.Item(ActionCode.OPEN_START_SCREEN));
			ourNodes = Collections.unmodifiableList(ourNodes);
		}
		return ourNodes;
	}
}
