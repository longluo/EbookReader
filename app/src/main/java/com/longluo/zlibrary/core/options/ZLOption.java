package com.longluo.zlibrary.core.options;

public abstract class ZLOption {
	private final StringPair myId;
	protected String myDefaultStringValue;
	protected String mySpecialName;

	protected ZLOption(String group, String optionName, String defaultStringValue) {
		myId = new StringPair(group, optionName);
		myDefaultStringValue = defaultStringValue != null ? defaultStringValue : "";
	}

	public final void setSpecialName(String specialName) {
		mySpecialName = specialName;
	}

	public void saveSpecialValue() {
	}

	protected final String getConfigValue() {
		final Config config = Config.Instance();
		return config != null ? config.getValue(myId, myDefaultStringValue) : myDefaultStringValue;
	}

	protected final void setConfigValue(String value) {
		final Config config = Config.Instance();
		if (config != null) {
			if (!myDefaultStringValue.equals(value)) {
				config.setValue(myId, value);
			} else {
				config.unsetValue(myId);
			}
		}
	}
}
