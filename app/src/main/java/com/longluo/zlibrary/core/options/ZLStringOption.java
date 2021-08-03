package com.longluo.zlibrary.core.options;

public final class ZLStringOption extends ZLOption {
	public ZLStringOption(String group, String optionName, String defaultValue) {
		super(group, optionName, defaultValue);
	}

	public String getValue() {
		if (mySpecialName != null && !Config.Instance().isInitialized()) {
			return Config.Instance().getSpecialStringValue(mySpecialName, myDefaultStringValue);
		} else {
			return getConfigValue();
		}
	}

	public void setValue(String value) {
		if (value == null) {
			return;
		}
		if (mySpecialName != null) {
			Config.Instance().setSpecialStringValue(mySpecialName, value);
		}
		setConfigValue(value);
	}

	public void saveSpecialValue() {
		if (mySpecialName != null && Config.Instance().isInitialized()) {
			Config.Instance().setSpecialStringValue(mySpecialName, getValue());
		}
	}
}
