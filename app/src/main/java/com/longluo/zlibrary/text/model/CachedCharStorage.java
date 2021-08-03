package com.longluo.zlibrary.text.model;

import java.lang.ref.WeakReference;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public final class CachedCharStorage {
	protected final ArrayList<WeakReference<char[]>> myArray =
		new ArrayList<WeakReference<char[]>>();

	private final String myDirectoryName;
	private final String myFileExtension;

	public CachedCharStorage(String directoryName, String fileExtension, int blocksNumber) {
		myDirectoryName = directoryName + '/';
		myFileExtension = '.' + fileExtension;
		myArray.addAll(Collections.nCopies(blocksNumber, new WeakReference<char[]>(null)));
	}

	private String fileName(int index) {
		return myDirectoryName + index + myFileExtension;
	}

	public int size() {
		return myArray.size();
	}

	private String exceptionMessage(int index, String extra) {
		final StringBuilder buffer = new StringBuilder("Cannot read " + fileName(index));
		if (extra != null) {
			buffer.append("; ").append(extra);
		}
		buffer.append("\n");
		try {
			final File dir = new File(myDirectoryName);
			buffer.append("ts = ").append(System.currentTimeMillis()).append("\n");
			buffer.append("dir exists = ").append(dir.exists()).append("\n");
			for (File f : dir.listFiles()) {
				buffer.append(f.getName()).append(" :: ");
				buffer.append(f.length()).append(" :: ");
				buffer.append(f.lastModified()).append("\n");
			}
		} catch (Throwable t) {
			buffer.append(t.getClass().getName());
			buffer.append("\n");
			buffer.append(t.getMessage());
		}
		return buffer.toString();
	}

	public char[] block(int index) {
		if (index < 0 || index >= myArray.size()) {
			return null;
		}
		char[] block = myArray.get(index).get();
		if (block == null) {
			try {
				File file = new File(fileName(index));
				int size = (int)file.length();
				if (size < 0) {
					throw new CachedCharStorageException(exceptionMessage(index, "size = " + size));
				}
				block = new char[size / 2];
				InputStreamReader reader =
					new InputStreamReader(
						new FileInputStream(file),
						"UTF-16LE"
					);
				final int rd = reader.read(block);
				if (rd != block.length) {
					throw new CachedCharStorageException(exceptionMessage(index, "; " + rd + " != " + block.length));
				}
				reader.close();
			} catch (IOException e) {
				throw new CachedCharStorageException(exceptionMessage(index, null), e);
			}
			myArray.set(index, new WeakReference<char[]>(block));
		}
		return block;
	}
}
