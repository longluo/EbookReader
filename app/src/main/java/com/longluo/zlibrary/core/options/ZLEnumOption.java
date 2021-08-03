package com.longluo.zlibrary.core.options;

public final class ZLEnumOption<T extends Enum<T>> extends ZLOption {
	private T myValue;
	private String myStringValue;
	private Class<T> myEnumClass;

	public ZLEnumOption(String group, String optionName, T defaultValue) {
		super(group, optionName, String.valueOf(defaultValue));
		myEnumClass = defaultValue.getDeclaringClass();
	}

	public T getValue() {
		final String stringValue = getConfigValue();
		if (!stringValue.equals(myStringValue)) {
			myStringValue = stringValue;
			try {
				myValue = T.valueOf(myEnumClass, stringValue);
			} catch (Throwable t) {
			}
		}
		return myValue;
	}

	public void setValue(T value) {
		if (value == null) {
			return;
		}
		myValue = value;
		myStringValue = String.valueOf(value);
		setConfigValue(myStringValue);
	}
}
