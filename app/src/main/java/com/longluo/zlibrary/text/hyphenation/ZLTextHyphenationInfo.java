package com.longluo.zlibrary.text.hyphenation;

public final class ZLTextHyphenationInfo {
	final boolean[] Mask;

	public ZLTextHyphenationInfo(int length) {
		Mask = new boolean[length - 1];
	}

	public boolean isHyphenationPossible(int position) {
		return (position < Mask.length && Mask[position]);
	}
}
