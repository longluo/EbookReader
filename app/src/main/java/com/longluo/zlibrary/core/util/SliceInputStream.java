package com.longluo.zlibrary.core.util;

import java.io.IOException;
import java.io.InputStream;

public class SliceInputStream extends InputStreamWithOffset {
	private final int myStart;
	private final int myLength;

	public SliceInputStream(InputStream base, int start, int length) throws IOException {
		super(base);
		baseSkip(start);
		myStart = start;
		myLength = length;
	}

	@Override
	public int read() throws IOException {
		if (offset() >= myLength) {
			return -1;
		}
		return super.read();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		final int maxbytes = myLength - offset();
		if (maxbytes <= 0) {
			return -1;
		}
		return super.read(b, off, Math.min(len, maxbytes));
	}

	@Override
	public long skip(long n) throws IOException {
		return super.skip(Math.min(n, Math.max(myLength - offset(), 0)));
	}

	@Override
	public int available() throws IOException {
		return Math.min(super.available(), Math.max(myLength - offset(), 0));
	}

	@Override
	public int offset() {
		return super.offset() - myStart;
	}
}
