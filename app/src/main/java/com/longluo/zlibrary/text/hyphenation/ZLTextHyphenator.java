package com.longluo.zlibrary.text.hyphenation;

import java.util.List;

import com.longluo.zlibrary.text.view.ZLTextWord;

public abstract class ZLTextHyphenator {
	private static ZLTextHyphenator ourInstance;

	public static ZLTextHyphenator Instance() {
		if (ourInstance == null) {
			ourInstance = new ZLTextTeXHyphenator();
		}
		return ourInstance;
	}

	public static void deleteInstance() {
		if (ourInstance != null) {
			ourInstance.unload();
			ourInstance = null;
		}
	}

	protected ZLTextHyphenator() {
	}

	public abstract List<String> languageCodes();
	public abstract void load(final String languageCode);
	public abstract void unload();

	public ZLTextHyphenationInfo getInfo(final ZLTextWord word) {
		final int len = word.Length;
		final boolean[] isLetter = new boolean[len];
		final char[] pattern = new char[len + 2];
		final char[] data = word.Data;
		pattern[0] = ' ';
		for (int i = 0, j = word.Offset; i < len; ++i, ++j) {
			char character = data[j];
			if (character == '\'' || character == '^' || Character.isLetter(character)) {
				isLetter[i] = true;
				pattern[i + 1] = Character.toLowerCase(character);
			} else {
				pattern[i + 1] = ' ';
			}
		}
		pattern[len + 1] = ' ';

		final ZLTextHyphenationInfo info = new ZLTextHyphenationInfo(len + 2);
		final boolean[] mask = info.Mask;
		hyphenate(pattern, mask, len + 2);
		for (int i = 0, j = word.Offset - 1; i <= len; ++i, ++j) {
			if ((i < 2) || (i > len - 2)) {
				mask[i] = false;
			} else {
				switch (data[j]) {
					case (char)0xAD: // soft hyphen
						mask[i] = true;
						break;
					case '-':
						mask[i] = (i >= 3)
							&& isLetter[i - 3]
							&& isLetter[i - 2]
							&& isLetter[i]
							&& isLetter[i + 1];
						break;
					default:
						mask[i] = mask[i]
							&& isLetter[i - 2]
							&& isLetter[i - 1]
							&& isLetter[i]
							&& isLetter[i + 1];
						break;
				}
			}
		}

		return info;
	}

	protected abstract void hyphenate(char[] stringToHyphenate, boolean[] mask, int length);
}
