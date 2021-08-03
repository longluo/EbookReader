package com.longluo.zlibrary.text.model;

public class ZLTextMark implements Comparable<ZLTextMark> {
	public final int ParagraphIndex;
	public final int Offset;
	public final int Length;

	public ZLTextMark(int paragraphIndex, int offset, int length) {
		ParagraphIndex = paragraphIndex;
		Offset = offset;
		Length = length;
	}

	public ZLTextMark(final ZLTextMark mark) {
		ParagraphIndex = mark.ParagraphIndex;
		Offset = mark.Offset;
		Length = mark.Length;
	}

	public int compareTo(ZLTextMark mark) {
		final int diff = ParagraphIndex - mark.ParagraphIndex;
		return diff != 0 ? diff : Offset - mark.Offset;
	}

	public String toString() {
		return ParagraphIndex + " " + Offset + " " + Length;
	}
}
