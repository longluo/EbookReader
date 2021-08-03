package com.longluo.ebookreader.util;

import java.util.*;

import com.longluo.zlibrary.text.view.*;
import com.longluo.ebookreader.bookmodel.FBTextKind;

public final class AutoTextSnippet implements TextSnippet {
	private final ZLTextPosition myStart;
	private final ZLTextPosition myEnd;
	private final String myText;

	public final boolean IsEndOfText;

	public AutoTextSnippet(ZLTextWordCursor start, int maxChars) {
		System.err.println("AutoTextSnippet " + maxChars);
		final ZLTextWordCursor cursor = new ZLTextWordCursor(start);

		final Buffer buffer = new Buffer(cursor);
		final Buffer sentenceBuffer = new Buffer(cursor);
		final Buffer phraseBuffer = new Buffer(cursor);

		int wordCounter = 0;
		int sentenceCounter = 0;
		int storedWordCounter = 0;
		boolean lineIsNonEmpty = false;
		boolean appendLineBreak = false;
mainLoop:
		while (buffer.Builder.length() + sentenceBuffer.Builder.length() + phraseBuffer.Builder.length() < maxChars && sentenceCounter < maxChars / 20) {
			while (cursor.isEndOfParagraph()) {
				if (!cursor.nextParagraph()) {
					break mainLoop;
				}
				if (!buffer.isEmpty() && cursor.getParagraphCursor().isLikeEndOfSection()) {
					break mainLoop;
				}
				if (!phraseBuffer.isEmpty()) {
					sentenceBuffer.append(phraseBuffer);
				}
				if (!sentenceBuffer.isEmpty()) {
					if (appendLineBreak) {
						buffer.append("\n");
					}
					buffer.append(sentenceBuffer);
					++sentenceCounter;
					storedWordCounter = wordCounter;
				}
				lineIsNonEmpty = false;
				if (!buffer.isEmpty()) {
					appendLineBreak = true;
				}
			}
			final ZLTextElement element = cursor.getElement();
			if (element == ZLTextElement.HSpace) {
				if (lineIsNonEmpty) {
					phraseBuffer.append(" ");
				}
			} else if (element == ZLTextElement.NBSpace) {
				if (lineIsNonEmpty) {
					phraseBuffer.append("\240");
				}
			} else if (element instanceof ZLTextWord) {
				final ZLTextWord word = (ZLTextWord)element;
				phraseBuffer.Builder.append(word.Data, word.Offset, word.Length);
				phraseBuffer.Cursor.setCursor(cursor);
				phraseBuffer.Cursor.setCharIndex(word.Length);
				++wordCounter;
				lineIsNonEmpty = true;
				switch (word.Data[word.Offset + word.Length - 1]) {
					case ',':
					case ':':
					case ';':
					case ')':
						sentenceBuffer.append(phraseBuffer);
						break;
					case '.':
					case '!':
					case '?':
						++sentenceCounter;
						if (appendLineBreak) {
							buffer.append("\n");
							appendLineBreak = false;
						}
						sentenceBuffer.append(phraseBuffer);
						buffer.append(sentenceBuffer);
						storedWordCounter = wordCounter;
						break;
				}
			} else if (element instanceof ZLTextControlElement) {
				final ZLTextControlElement control = (ZLTextControlElement)element;
				if (control.IsStart) {
					switch (control.Kind) {
						case FBTextKind.H1:
						case FBTextKind.H2:
							if (!buffer.isEmpty()) {
								break mainLoop;
							}
							break;
					}
				}
			}
			cursor.nextWord();
		}

		IsEndOfText =
			cursor.isEndOfText() || cursor.getParagraphCursor().isLikeEndOfSection();

		if (IsEndOfText) {
			sentenceBuffer.append(phraseBuffer);
			if (appendLineBreak) {
				buffer.append("\n");
			}
			buffer.append(sentenceBuffer);
		} else if (storedWordCounter < 4 || sentenceCounter < maxChars / 30) {
			if (sentenceBuffer.isEmpty()) {
				sentenceBuffer.append(phraseBuffer);
			}
			if (appendLineBreak) {
				buffer.append("\n");
			}
			buffer.append(sentenceBuffer);
		}

		myStart = new ZLTextFixedPosition(start);
		myEnd = buffer.Cursor;
		myText = buffer.Builder.toString();
	}

	private static class Buffer {
		final StringBuilder Builder = new StringBuilder();
		final ZLTextWordCursor Cursor;

		Buffer(ZLTextWordCursor cursor) {
			Cursor = new ZLTextWordCursor(cursor);
		}

		boolean isEmpty() {
			return Builder.length() == 0;
		}

		void append(Buffer buffer) {
			Builder.append(buffer.Builder);
			Cursor.setCursor(buffer.Cursor);
			buffer.Builder.delete(0, buffer.Builder.length());
		}

		void append(CharSequence data) {
			Builder.append(data);
		}
	}

	public ZLTextPosition getStart() {
		return myStart;
	}

	public ZLTextPosition getEnd() {
		return myEnd;
	}

	public String getText() {
		return myText;
	}
}
