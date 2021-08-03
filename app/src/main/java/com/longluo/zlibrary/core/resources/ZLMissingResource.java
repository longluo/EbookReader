package com.longluo.zlibrary.core.resources;

final class ZLMissingResource extends ZLResource {
	static final String Value = "????????";
	static final ZLMissingResource Instance = new ZLMissingResource();

	private ZLMissingResource() {
		super(Value);
	}

	@Override
	public ZLResource getResource(String key) {
		return this;
	}

	@Override
	public boolean hasValue() {
		return false;
	}

	@Override
	public String getValue() {
		return Value;
	}

	@Override
	public String getValue(int condition) {
		return Value;
	}
}
