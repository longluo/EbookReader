package com.longluo.zlibrary.core.options;

public final class ZLBooleanOption extends ZLOption {
	private final boolean myDefaultValue;

	public ZLBooleanOption(String group, String optionName, boolean defaultValue) {
		super(group, optionName, defaultValue ? "true" : "false");
		myDefaultValue = defaultValue;
	}

	public boolean getValue() {
		if (mySpecialName != null && !Config.Instance().isInitialized()) {
			return Config.Instance().getSpecialBooleanValue(mySpecialName, myDefaultValue);
		} else {
			return "true".equals(getConfigValue());
		}
	}

	public void setValue(boolean value) {
		if (mySpecialName != null) {
			Config.Instance().setSpecialBooleanValue(mySpecialName, value);
		}
		setConfigValue(value ? "true" : "false");
	}

	public void saveSpecialValue() {
		if (mySpecialName != null && Config.Instance().isInitialized()) {
			Config.Instance().setSpecialBooleanValue(mySpecialName, getValue());
		}
	}
}
