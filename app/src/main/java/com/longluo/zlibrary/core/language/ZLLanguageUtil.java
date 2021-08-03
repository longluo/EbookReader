package com.longluo.zlibrary.core.language;

import java.util.*;

import com.longluo.zlibrary.core.filesystem.*;

public abstract class ZLLanguageUtil {
	private static ArrayList<String> ourLanguageCodes = new ArrayList<String>();

	private ZLLanguageUtil() {
	}

	public static String defaultLanguageCode() {
		return Locale.getDefault().getLanguage();
	}

	public static List<String> languageCodes() {
		if (ourLanguageCodes.isEmpty()) {
			TreeSet<String> codes = new TreeSet<String>();
			for (ZLFile file : patternsFile().children()) {
				String name = file.getShortName();
				final int index = name.indexOf("_");
				if (index != -1) {
					String str = name.substring(0, index);
					if (!codes.contains(str)) {
						codes.add(str);
					}
				}
			}
			codes.add("id");
			codes.add("de-traditional");

			ourLanguageCodes.addAll(codes);
		}

		return Collections.unmodifiableList(ourLanguageCodes);
	}

	public static ZLFile patternsFile() {
		return ZLResourceFile.createResourceFile("languagePatterns");
	}
}
