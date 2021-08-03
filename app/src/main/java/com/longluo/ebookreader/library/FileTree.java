package com.longluo.ebookreader.library;

import java.util.*;

import com.longluo.util.NaturalOrderComparator;
import com.longluo.util.Pair;

import com.longluo.zlibrary.core.filesystem.ZLFile;
import com.longluo.zlibrary.core.image.ZLImage;

import com.longluo.ebookreader.book.*;
import com.longluo.ebookreader.tree.FBTree;

public class FileTree extends LibraryTree {
	private static final NaturalOrderComparator ourNaturalOrderComparator =
		new NaturalOrderComparator();

	private static final Comparator<ZLFile> ourFileComparator = new Comparator<ZLFile>() {
		public int compare(ZLFile file0, ZLFile file1) {
			final boolean isDir = file0.isDirectory();
			if (isDir != file1.isDirectory()) {
				return isDir ? -1 : 1;
			}
			return ourNaturalOrderComparator.compare(file0.getShortName(), file1.getShortName());
		}
	};

	private final ZLFile myFile;
	private final String myName;
	private final String mySummary;
	private final boolean myIsSelectable;

	FileTree(LibraryTree parent, ZLFile file, String name, String summary) {
		super(parent);
		myFile = file;
		myName = name;
		mySummary = summary;
		myIsSelectable = false;
	}

	public FileTree(FileTree parent, ZLFile file) {
		super(parent);
		myFile = file;
		myName = null;
		mySummary = null;
		myIsSelectable = true;
	}

	@Override
	public String getName() {
		return myName != null ? myName : myFile.getShortName();
	}

	@Override
	public Pair<String,String> getTreeTitle() {
		return new Pair(myFile.getPath(), null);
	}

	@Override
	protected String getStringId() {
		return myFile.getShortName();
	}

	@Override
	public String getSummary() {
		if (mySummary != null) {
			return mySummary;
		}

		final Book book = getBook();
		if (book != null) {
			return book.getTitle();
		}

		return null;
	}

	@Override
	public boolean isSelectable() {
		return myIsSelectable;
	}

	@Override
	public ZLImage createCover() {
		return CoverUtil.getCover(getBook(), PluginCollection);
	}

	public ZLFile getFile() {
		return myFile;
	}

	private Object myBook;
	private static final Object NULL_BOOK = new Object();

	@Override
	public Book getBook() {
		if (myBook == null) {
			myBook = Collection.getBookByFile(myFile.getPath());
			if (myBook == null) {
				myBook = NULL_BOOK;
			}
		}
		return myBook instanceof Book ? (Book)myBook : null;
	}

	@Override
	public boolean containsBook(Book book) {
		if (book == null) {
			return false;
		}
		if (myFile.isDirectory()) {
			String prefix = myFile.getPath();
			if (!prefix.endsWith("/")) {
				prefix += "/";
			}
			return book.getPath().startsWith(prefix);
		} else if (myFile.isArchive()) {
			return book.getPath().startsWith(myFile.getPath() + ":");
		} else {
			return book.equals(getBook());
		}
	}

	@Override
	public Status getOpeningStatus() {
		if (!myFile.isReadable()) {
			return Status.CANNOT_OPEN;
		}
		return Status.ALWAYS_RELOAD_BEFORE_OPENING;
	}

	@Override
	public String getOpeningStatusMessage() {
		return getOpeningStatus() == Status.CANNOT_OPEN ? "permissionDenied" : null;
	}

	@Override
	public void waitForOpening() {
		if (getBook() != null) {
			return;
		}
		final TreeSet<ZLFile> set = new TreeSet<ZLFile>(ourFileComparator);
		for (ZLFile file : myFile.children()) {
			if (file.isDirectory() || file.isArchive() ||
				Collection.getBookByFile(file.getPath()) != null) {
				set.add(file);
			}
		}
		clear();
		for (ZLFile file : set) {
			new FileTree(this, file);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof FileTree)) {
			return true;
		}
		return myFile.equals(((FileTree)o).myFile);
	}

	@Override
	public int compareTo(FBTree tree) {
		return ourFileComparator.compare(myFile, ((FileTree)tree).myFile);
	}
}
