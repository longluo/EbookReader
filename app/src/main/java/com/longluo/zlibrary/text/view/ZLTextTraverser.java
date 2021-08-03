package com.longluo.zlibrary.text.view;

public abstract class ZLTextTraverser {
	private final ZLTextView myView;

	protected ZLTextTraverser(ZLTextView view) {
		myView = view;
	}

	protected abstract void processWord(ZLTextWord word);
	protected abstract void processControlElement(ZLTextControlElement control);
	protected abstract void processSpace();
	protected abstract void processNbSpace();
	protected abstract void processEndOfParagraph();

	public void traverse(ZLTextPosition from, ZLTextPosition to) {
		final int fromParagraph = from.getParagraphIndex();
		final int toParagraph = to.getParagraphIndex();
		ZLTextParagraphCursor cursor = myView.cursor(fromParagraph);
		for (int i = fromParagraph; i <= toParagraph; ++i) {
			final int fromElement = i == fromParagraph ? from.getElementIndex() : 0;
			final int toElement = i == toParagraph ? to.getElementIndex() : cursor.getParagraphLength() - 1;

			for (int j = fromElement; j <= toElement; j++) {
				final ZLTextElement element = cursor.getElement(j);
				if (element == ZLTextElement.HSpace) {
					processSpace();
				} else if (element == ZLTextElement.NBSpace) {
					processNbSpace();
				} else if (element instanceof ZLTextWord) {
					processWord((ZLTextWord)element);
				}
			}
			if (i < toParagraph) {
				processEndOfParagraph();
				cursor = cursor.next();
			}
		}
	}
}
