package com.longluo.zlibrary.core.network;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.json.simple.JSONValue;

class BearerAuthenticationException extends RuntimeException {
	public final String Realm;
	public final Map<String,String> Params = new HashMap<String,String>();

	BearerAuthenticationException(String realm, HttpEntity entity) {
		super("Authentication failed");
		Realm = realm;
		try {
			Params.putAll((Map)JSONValue.parse(new InputStreamReader(entity.getContent())));
		} catch (Exception e) {
		}
	}
}
