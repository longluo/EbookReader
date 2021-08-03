package com.longluo.zlibrary.core.network;

import java.io.*;

import org.json.simple.JSONValue;

public abstract class JsonRequest2 extends ZLNetworkRequest.PostWithBody {
	public JsonRequest2(String url, Object data) {
		super(url, JSONValue.toJSONString(data), false);
	}

	@Override
	public void handleStream(InputStream stream, int length) throws IOException, ZLNetworkException {
		processResponse(JSONValue.parse(new InputStreamReader(stream)));
	}

	protected abstract void processResponse(Object response);
}
