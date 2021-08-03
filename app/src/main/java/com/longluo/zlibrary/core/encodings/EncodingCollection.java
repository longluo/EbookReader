package com.longluo.zlibrary.core.encodings;

import java.util.List;

public abstract class EncodingCollection {
	public abstract List<Encoding> encodings();
	public abstract Encoding getEncoding(String alias);
	public abstract Encoding getEncoding(int code);
}
