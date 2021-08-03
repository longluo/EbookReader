package com.longluo.zlibrary.text.view;

final class ZLTextLineInfo {
	final ZLTextParagraphCursor ParagraphCursor;
	final int ParagraphCursorLength;

	final int StartElementIndex;
	final int StartCharIndex;
	int RealStartElementIndex;
	int RealStartCharIndex;
	int EndElementIndex;
	int EndCharIndex;

	boolean IsVisible;
	int LeftIndent;
	int Width;
	int Height;
	int Descent;
	int VSpaceBefore;
	int VSpaceAfter;
	boolean PreviousInfoUsed;
	int SpaceCounter;
	ZLTextStyle StartStyle;

	ZLTextLineInfo(ZLTextParagraphCursor paragraphCursor, int elementIndex, int charIndex, ZLTextStyle style) {
		ParagraphCursor = paragraphCursor;
		ParagraphCursorLength = paragraphCursor.getParagraphLength();

		StartElementIndex = elementIndex;
		StartCharIndex = charIndex;
		RealStartElementIndex = elementIndex;
		RealStartCharIndex = charIndex;
		EndElementIndex = elementIndex;
		EndCharIndex = charIndex;

		StartStyle = style;
	}

	boolean isEndOfParagraph() {
		return EndElementIndex == ParagraphCursorLength;
	}

	void adjust(ZLTextLineInfo previous) {
		if (!PreviousInfoUsed && previous != null) {
			Height -= Math.min(previous.VSpaceAfter, VSpaceBefore);
			PreviousInfoUsed = true;
		}
	}

	@Override
	public boolean equals(Object o) {
		ZLTextLineInfo info = (ZLTextLineInfo)o;
		return
			(ParagraphCursor == info.ParagraphCursor) &&
			(StartElementIndex == info.StartElementIndex) &&
			(StartCharIndex == info.StartCharIndex);
	}

	@Override
	public int hashCode() {
		return ParagraphCursor.hashCode() + StartElementIndex + 239 * StartCharIndex;
	}
}
