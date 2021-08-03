package com.longluo.zlibrary.text.view;

import java.util.List;
import java.util.Map;

import com.longluo.zlibrary.text.model.ExtensionEntry;

public abstract class ExtensionElementManager {
	final List<? extends ExtensionElement> getElements(ExtensionEntry entry) {
		return getElements(entry.Type, entry.Data);
	}

	protected abstract List<? extends ExtensionElement> getElements(String type, Map<String,String> data);
}
