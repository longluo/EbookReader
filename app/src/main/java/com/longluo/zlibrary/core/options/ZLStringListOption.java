package com.longluo.zlibrary.core.options;

import java.util.*;

import com.longluo.zlibrary.core.util.MiscUtil;

public class ZLStringListOption extends ZLOption {
	private final String myDelimiter;
	private List<String> myValue;
	private String myStringValue;

	public ZLStringListOption(String group, String optionName, List<String> defaultValue, String delimiter) {
		super(group, optionName, MiscUtil.join(defaultValue, delimiter));
		myDelimiter = delimiter;
	}

	public ZLStringListOption(String group, String optionName, String defaultValue, String delimiter) {
		this(
			group, optionName, defaultValue != null
				? Collections.singletonList(defaultValue)
				: Collections.<String>emptyList(),
			delimiter
		);
	}

	public List<String> getValue() {
		final String stringValue = getConfigValue();
		if (!stringValue.equals(myStringValue)) {
			myStringValue = stringValue;
			myValue = MiscUtil.split(stringValue, myDelimiter);
		}
		return myValue;
	}

	public void setValue(List<String> value) {
		if (value == null) {
			value = Collections.emptyList();
		}
		if (value.equals(myValue)) {
			return;
		}
		myValue = new ArrayList<String>(value);
		myStringValue = MiscUtil.join(value, myDelimiter);
		setConfigValue(myStringValue);
	}
}
