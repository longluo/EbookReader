package com.longluo.zlibrary.core.encodings;

import java.util.List;
import java.util.Collections;

public final class AutoEncodingCollection extends EncodingCollection {
	private final Encoding myEncoding = new Encoding(null, "auto", "auto");

	public List<Encoding> encodings() {
		return Collections.singletonList(myEncoding);
	}

	public Encoding getEncoding(String alias) {
		return myEncoding;
	}

	public Encoding getEncoding(int code) {
		return myEncoding;
	}
}
