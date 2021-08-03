package com.longluo.zlibrary.text.view;

public class ZLTextFixedPosition extends ZLTextPosition {
	public final int ParagraphIndex;
	public final int ElementIndex;
	public final int CharIndex;

	public ZLTextFixedPosition(int paragraphIndex, int elementIndex, int charIndex) {
		ParagraphIndex = paragraphIndex;
		ElementIndex = elementIndex;
		CharIndex = charIndex;
	}

	public ZLTextFixedPosition(ZLTextPosition position) {
		ParagraphIndex = position.getParagraphIndex();
		ElementIndex = position.getElementIndex();
		CharIndex = position.getCharIndex();
	}

	public final int getParagraphIndex() {
		return ParagraphIndex;
	}

	public final int getElementIndex() {
		return ElementIndex;
	}

	public final int getCharIndex() {
		return CharIndex;
	}

	public static class WithTimestamp extends ZLTextFixedPosition {
		public final long Timestamp;

		public WithTimestamp(int paragraphIndex, int elementIndex, int charIndex, Long stamp) {
			super(paragraphIndex, elementIndex, charIndex);
			Timestamp = stamp != null ? stamp : -1;
		}

		@Override
		public String toString() {
			return super.toString() + "; timestamp = " + Timestamp;
		}
	}
}
