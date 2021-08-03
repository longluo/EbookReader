package com.longluo.zlibrary.core.options;

public final class ZLIntegerOption extends ZLOption {
	private int myValue;
	private String myStringValue;

	public ZLIntegerOption(String group, String optionName, int defaultValue) {
		super(group, optionName, String.valueOf(defaultValue));
	}

	public int getValue() {
		final String stringValue = getConfigValue();
		if (!stringValue.equals(myStringValue)) {
			myStringValue = stringValue;
			try {
				myValue = Integer.parseInt(stringValue);
			} catch (NumberFormatException e) {
			}
		}
		return myValue;
	}

	public void setValue(int value) {
		myValue = value;
		myStringValue = String.valueOf(value);
		setConfigValue(myStringValue);
	}
}
