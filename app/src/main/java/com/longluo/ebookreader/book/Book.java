package com.longluo.ebookreader.book;

public final class Book extends AbstractBook {
	private final String myPath;

	public Book(long id, String path, String title, String encoding, String language) {
		super(id, title, encoding, language);
		if (path == null) {
			throw new IllegalArgumentException("Creating book with no file");
		}
		myPath = path;
	}

	@Override
	public String getPath() {
		return myPath;
	}

	@Override
	public int hashCode() {
		return myPath.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Book)) {
			return false;
		}
		return myPath.equals(((Book)o).myPath);
	}
}
