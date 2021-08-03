package com.longluo.ebookreader.bookmodel;

import com.longluo.zlibrary.core.tree.ZLTree;

import com.longluo.zlibrary.text.model.ZLTextModel;

public class TOCTree extends ZLTree<TOCTree> {
	private String myText;
	private Reference myReference;

	protected TOCTree() {
		super();
	}

	public TOCTree(TOCTree parent) {
		super(parent);
	}

	public final String getText() {
		return myText;
	}

	// faster replacement for
	// return text.trim().replaceAll("[\t ]+", " ");
	private static String trim(String text) {
		final char[] data = text.toCharArray();
		int count = 0;
		int shift = 0;
		boolean changed = false;
		char space = ' ';
		for (int i = 0; i < data.length; ++i) {
			final char ch = data[i];
			if (ch == ' ' || ch == '\t') {
				++count;
				space = ch;
			} else {
				if (count > 0) {
					if (count == i) {
						shift += count;
						changed = true;
					} else {
						shift += count - 1;
						if (shift > 0 || space == '\t') {
							data[i - shift - 1] = ' ';
							changed = true;
						}
					}
					count = 0;
				}
				if (shift > 0) {
					data[i - shift] = data[i];
				}
			}
		}
		if (count > 0) {
			changed = true;
			shift += count;
		}
		return changed ? new String(data, 0, data.length - shift) : text;
	}

	public final void setText(String text) {
		myText = text != null ? trim(text) : null;
	}

	public Reference getReference() {
		return myReference;
	}

	public void setReference(ZLTextModel model, int reference) {
		myReference = new Reference(reference, model);
	}

	public static class Reference {
		public final int ParagraphIndex;
		public final ZLTextModel Model;

		public Reference(final int paragraphIndex, final ZLTextModel model) {
			ParagraphIndex = paragraphIndex;
			Model = model;
		}
	}
}
