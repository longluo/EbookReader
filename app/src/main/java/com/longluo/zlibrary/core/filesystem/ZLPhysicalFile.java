package com.longluo.zlibrary.core.filesystem;

import java.util.*;
import java.io.*;

public final class ZLPhysicalFile extends ZLFile {
	private final File myFile;

	ZLPhysicalFile(String path) {
		this(new File(path));
	}

	public ZLPhysicalFile(File file) {
		myFile = file;
		init();
	}

	@Override
	public boolean exists() {
		return myFile.exists();
	}

	@Override
	public long size() {
		return myFile.length();
	}

	@Override
	public long lastModified() {
		return myFile.lastModified();
	}

	private Boolean myIsDirectory;
	@Override
	public boolean isDirectory() {
		if (myIsDirectory == null) {
			myIsDirectory = myFile.isDirectory();
		}
		return myIsDirectory;
	}

	@Override
	public boolean isReadable() {
		return myFile.canRead();
	}

	public boolean delete() {
		return myFile.delete();
	}

	public File javaFile() {
		return myFile;
	}

	private String myPath;
	@Override
	public String getPath() {
		if (myPath == null) {
			try {
				myPath = myFile.getCanonicalPath();
			} catch (Exception e) {
				// should be never thrown
				myPath = myFile.getPath();
			}
		}
		return myPath;
	}

	@Override
	public String getLongName() {
		return isDirectory() ? getPath() : myFile.getName();
	}

	@Override
	public ZLFile getParent() {
		return isDirectory() ? null : new ZLPhysicalFile(myFile.getParent());
	}

	@Override
	public ZLPhysicalFile getPhysicalFile() {
		return this;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(myFile);
	}

	protected List<ZLFile> directoryEntries() {
		File[] subFiles = myFile.listFiles();
		if (subFiles == null || subFiles.length == 0) {
			return Collections.emptyList();
		}

		ArrayList<ZLFile> entries = new ArrayList<ZLFile>(subFiles.length);
		for (File f : subFiles) {
			if (!f.getName().startsWith(".")) {
				entries.add(new ZLPhysicalFile(f));
			}
		}
		return entries;
	}
}
