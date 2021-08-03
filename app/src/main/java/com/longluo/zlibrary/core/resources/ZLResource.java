package com.longluo.zlibrary.core.resources;

import java.util.*;

import com.longluo.zlibrary.core.filesystem.ZLFile;
import com.longluo.zlibrary.core.filesystem.ZLResourceFile;
import com.longluo.zlibrary.core.language.Language;
import com.longluo.zlibrary.core.options.ZLStringOption;

abstract public class ZLResource {
	public final String Name;

	private static final List<String> ourLanguageCodes = new LinkedList<String>();
	public static List<String> languageCodes() {
		synchronized (ourLanguageCodes) {
			if (ourLanguageCodes.isEmpty()) {
				final ZLFile dir = ZLResourceFile.createResourceFile("resources/application");
				final List<ZLFile> children = dir.children();
				for (ZLFile file : children) {
					final String name = file.getShortName();
					final String postfix = ".xml";
					if (name.endsWith(postfix) && !"neutral.xml".equals(name)) {
						ourLanguageCodes.add(name.substring(0, name.length() - postfix.length()));
					}
				}
			}
		}
		return Collections.unmodifiableList(ourLanguageCodes);
	}

	public static List<Language> interfaceLanguages() {
		final List<Language> allLanguages = new LinkedList<Language>();
		final ZLResource resource = ZLResource.resource("language-self");
		for (String c : languageCodes()) {
			allLanguages.add(new Language(c, resource));
		}
		Collections.sort(allLanguages);
		allLanguages.add(0, new Language(Language.SYSTEM_CODE));
		return allLanguages;
	}

	private static final ZLStringOption ourLanguageOption =
		new ZLStringOption("LookNFeel", "Language", Language.SYSTEM_CODE);
	public static ZLStringOption getLanguageOption() {
		return ourLanguageOption;
	}
	public static String getLanguage() {
		final String lang = getLanguageOption().getValue();
		return Language.SYSTEM_CODE.equals(lang) ? Locale.getDefault().getLanguage() : lang;
	}

	public static ZLResource resource(String key) {
		ZLTreeResource.buildTree();
		if (ZLTreeResource.ourRoot == null) {
			return ZLMissingResource.Instance;
		}
		try {
			return ZLTreeResource.ourRoot.getResource(key);
		} finally {
		}
	}

	protected ZLResource(String name) {
		Name = name;
	}

	abstract public boolean hasValue();
	abstract public String getValue();
	abstract public String getValue(int condition);
	abstract public ZLResource getResource(String key);
}
