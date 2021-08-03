package com.longluo.zlibrary.core.util;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamWithOffset extends InputStream {
	private final InputStream myDecoratedStream;
	private int myOffset = 0;

	public InputStreamWithOffset(InputStream stream) {
		myDecoratedStream = stream;
	}

	@Override
	public int available() throws IOException {
		return myDecoratedStream.available();
	}

	@Override
	public long skip(long n) throws IOException {
		long shift = myDecoratedStream.skip(n);
		if (shift > 0) {
			myOffset += (int)shift;
		}
		while (shift < n && read() != -1) {
			++shift;
		}
		return shift;
	}

	// does not call virtual methods
	protected final long baseSkip(long n) throws IOException {
		long shift = myDecoratedStream.skip(n);
		while (shift < n && myDecoratedStream.read() != -1) {
			++shift;
		}
		myOffset += shift;
		return shift;
	}

	@Override
	public int read() throws IOException {
		int result = myDecoratedStream.read();
		if (result != -1) {
			++myOffset;
		}
		return result;
	}

	@Override
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		final int shift = myDecoratedStream.read(b, off, len);
		if (shift > 0) {
			myOffset += shift;
		}
		return shift;
	}

	public int offset() {
		return myOffset;
	}

	@Override
	public void close() throws IOException {
		myOffset = 0;
		myDecoratedStream.close();
	}
}
