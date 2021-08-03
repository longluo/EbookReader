package com.longluo.zlibrary.text.view;

public final class ZLTextElementArea extends ZLTextFixedPosition {
	public final int XStart;
	public final int XEnd;
	public final int YStart;
	public final int YEnd;
	public final int ColumnIndex;

	final int Length;
	final boolean AddHyphenationSign;
	final boolean ChangeStyle;
	final ZLTextStyle Style;
	final ZLTextElement Element;

	private final boolean myIsLastInElement;

	ZLTextElementArea(int paragraphIndex, int elementIndex, int charIndex, int length, boolean lastInElement, boolean addHyphenationSign, boolean changeStyle, ZLTextStyle style, ZLTextElement element, int xStart, int xEnd, int yStart, int yEnd, int columnIndex) {
		super(paragraphIndex, elementIndex, charIndex);

		XStart = xStart;
		XEnd = xEnd;
		YStart = yStart;
		YEnd = yEnd;
		ColumnIndex = columnIndex;

		Length = length;
		myIsLastInElement = lastInElement;

		AddHyphenationSign = addHyphenationSign;
		ChangeStyle = changeStyle;
		Style = style;
		Element = element;
	}

	boolean contains(int x, int y) {
		return (y >= YStart) && (y <= YEnd) && (x >= XStart) && (x <= XEnd);
	}

	boolean isFirstInElement() {
		return CharIndex == 0;
	}

	boolean isLastInElement() {
		return myIsLastInElement;
	}
}
