package com.longluo.android.ebookreader.api;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class MenuNode implements Cloneable, Serializable {
	public static final long serialVersionUID = 42L;

	public final String Code;
	public String OptionalTitle;

	private MenuNode(String code) {
		Code = code;
	}

	public abstract MenuNode clone();

	public static final class Item extends MenuNode {
		public static final long serialVersionUID = 43L;

		public final Integer IconId;

		public Item(String code, Integer iconId) {
			super(code);
			IconId = iconId;
		}

		public Item(String code) {
			this(code, null);
		}

		public Item clone() {
			return new Item(Code, IconId);
		}
	}

	public static class Submenu extends MenuNode {
		public static final long serialVersionUID = 44L;

		public final ArrayList<MenuNode> Children = new ArrayList<MenuNode>();

		public Submenu(String code) {
			super(code);
		}

		public Submenu clone() {
			final Submenu copy = new Submenu(Code);
			for (MenuNode node : Children) {
				copy.Children.add(node.clone());
			}
			return copy;
		}
	}
}
