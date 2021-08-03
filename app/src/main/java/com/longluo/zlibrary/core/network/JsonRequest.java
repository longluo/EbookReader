package com.longluo.zlibrary.core.network;

import java.io.*;

import org.json.simple.JSONValue;

public abstract class JsonRequest extends ZLNetworkRequest.PostWithMap {
	public JsonRequest(String url) {
		super(url);
	}

	@Override
	public void handleStream(InputStream stream, int length) throws IOException, ZLNetworkException {
		processResponse(JSONValue.parse(new InputStreamReader(stream)));
	}

	protected abstract void processResponse(Object response);
}
