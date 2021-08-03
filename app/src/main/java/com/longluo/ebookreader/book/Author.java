package com.longluo.ebookreader.book;

public final class Author implements Comparable<Author> {
	public static final Author NULL = new Author("", "");

	public static Author create(String name, String sortKey) {
		if (name == null) {
			return null;
		}
		String strippedName = name.trim();
		if (strippedName.length() == 0) {
			return null;
		}

		String strippedKey = sortKey != null ? sortKey.trim() : "";
		if (strippedKey.length() == 0) {
			int index = strippedName.lastIndexOf(' ');
			if (index == -1) {
				strippedKey = strippedName;
			} else {
				strippedKey = strippedName.substring(index + 1);
				while ((index >= 0) && (strippedName.charAt(index) == ' ')) {
					--index;
				}
				strippedName = strippedName.substring(0, index + 1) + ' ' + strippedKey;
			}
		}

		return new Author(strippedName, strippedKey);
	}

	public final String DisplayName;
	public final String SortKey;

	public Author(String displayName, String sortKey) {
		DisplayName = displayName;
		SortKey = sortKey.toLowerCase();
	}

	public static int hashCode(Author author) {
		return author == null ? 0 : author.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Author)) {
			return false;
		}
		Author a = (Author)o;
		return SortKey.equals(a.SortKey) && DisplayName.equals(a.DisplayName);
	}

	@Override
	public int hashCode() {
		return SortKey.hashCode() + DisplayName.hashCode();
	}

	@Override
	public int compareTo(Author other) {
		final int byKeys = SortKey.compareTo(other.SortKey);
		return byKeys != 0 ? byKeys : DisplayName.compareTo(other.DisplayName);
	}

	@Override
	public String toString() {
		return DisplayName + " (" + SortKey + ")";
	}
}
