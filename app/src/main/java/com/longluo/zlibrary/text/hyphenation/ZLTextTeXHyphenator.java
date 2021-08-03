package com.longluo.zlibrary.text.hyphenation;

import java.util.*;

import com.longluo.zlibrary.core.language.Language;
import com.longluo.zlibrary.core.language.ZLLanguageUtil;
import com.longluo.zlibrary.core.filesystem.ZLFile;
import com.longluo.zlibrary.core.filesystem.ZLResourceFile;

final class ZLTextTeXHyphenator extends ZLTextHyphenator {
	private final HashMap<ZLTextTeXHyphenationPattern,ZLTextTeXHyphenationPattern> myPatternTable =
		new HashMap<ZLTextTeXHyphenationPattern,ZLTextTeXHyphenationPattern>();
	private int myMaxPatternLength;
	private String myLanguage;

	void addPattern(ZLTextTeXHyphenationPattern pattern) {
		myPatternTable.put(pattern, pattern);
		if (myMaxPatternLength < pattern.length()) {
			myMaxPatternLength = pattern.length();
		}
	}

	private List<String> myLanguageCodes;
	public List<String> languageCodes() {
		if (myLanguageCodes == null) {
			final TreeSet<String> codes = new TreeSet<String>();
			final ZLFile patternsFile = ZLResourceFile.createResourceFile("hyphenationPatterns");
			for (ZLFile file : patternsFile.children()) {
				final String name = file.getShortName();
				if (name.endsWith(".pattern")) {
					codes.add(name.substring(0, name.length() - ".pattern".length()));
				}
			}

			codes.add("zh");
			myLanguageCodes = new ArrayList<String>(codes);
		}

		return Collections.unmodifiableList(myLanguageCodes);
	}

	public void load(String language) {
		if (language == null || Language.OTHER_CODE.equals(language)) {
			language = ZLLanguageUtil.defaultLanguageCode();
		}
		if (language == null || language.equals(myLanguage)) {
			return;
		}
		myLanguage = language;
		unload();

		if (language != null) {
			new ZLTextHyphenationReader(this).readQuietly(ZLResourceFile.createResourceFile(
				"hyphenationPatterns/" + language + ".pattern"
			));
		}
	}

	public void unload() {
		myPatternTable.clear();
		myMaxPatternLength = 0;
	}

	public void hyphenate(char[] stringToHyphenate, boolean[] mask, int length) {
		if (myPatternTable.isEmpty()) {
			for (int i = 0; i < length - 1; i++) {
				mask[i] = false;
			}
			return;
		}

		byte[] values = new byte[length + 1];

		final HashMap<ZLTextTeXHyphenationPattern,ZLTextTeXHyphenationPattern> table = myPatternTable;
		ZLTextTeXHyphenationPattern pattern =
			new ZLTextTeXHyphenationPattern(stringToHyphenate, 0, length, false);
		for (int offset = 0; offset < length - 1; offset++) {
			int len = Math.min(length - offset, myMaxPatternLength) + 1;
			pattern.update(stringToHyphenate, offset, len - 1);
			while (--len > 0) {
				pattern.reset(len);
				ZLTextTeXHyphenationPattern toApply =
					(ZLTextTeXHyphenationPattern)table.get(pattern);
				if (toApply != null) {
					toApply.apply(values, offset);
				}
			}
		}

		for (int i = 0; i < length - 1; i++) {
			mask[i] = (values[i + 1] % 2) == 1;
		}
	}
}
