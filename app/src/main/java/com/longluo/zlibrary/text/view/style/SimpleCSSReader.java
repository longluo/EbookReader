package com.longluo.zlibrary.text.view.style;

import java.io.*;
import java.util.*;

import com.longluo.zlibrary.core.filesystem.ZLFile;
import com.longluo.zlibrary.core.util.MiscUtil;

class SimpleCSSReader {
	private enum State {
		EXPECT_SELECTOR,
		EXPECT_OPEN_BRACKET,
		EXPECT_NAME,
		EXPECT_VALUE,
		READ_COMMENT,
	}

	private State myState;
	private State mySavedState;
	private Map<Integer,ZLTextNGStyleDescription> myDescriptionMap;
	private Map<String,String> myCurrentMap;
	private String mySelector;
	private String myName;

	Map<Integer,ZLTextNGStyleDescription> read(ZLFile file) {
		myDescriptionMap = new LinkedHashMap<Integer,ZLTextNGStyleDescription>();
		myState = State.EXPECT_SELECTOR;

		InputStream stream = null;
		try {
			stream = file.getInputStream();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String line;
			while ((line = reader.readLine()) != null) {
				for (String token : MiscUtil.smartSplit(line)) {
					processToken(token);
				}
			}
		} catch (IOException e) {
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		}

		return myDescriptionMap;
	}

	private void processToken(String token) {
		if (myState != State.READ_COMMENT && token.startsWith("/*")) {
			mySavedState = myState;
			myState = State.READ_COMMENT;
			return;
		}

		switch (myState) {
			case READ_COMMENT:
				if (token.endsWith("*/")) {
					myState = mySavedState;
				}
				break;
			case EXPECT_SELECTOR:
				mySelector = token;
				myState = State.EXPECT_OPEN_BRACKET;
				break;
			case EXPECT_OPEN_BRACKET:
				if ("{".equals(token)) {
					myCurrentMap = new HashMap<String,String>();
					myState = State.EXPECT_NAME;
				}
				break;
			case EXPECT_NAME:
				if ("}".equals(token)) {
					if (mySelector != null) {
						try {
							myDescriptionMap.put(
								Integer.valueOf(myCurrentMap.get("fbreader-id")),
								new ZLTextNGStyleDescription(mySelector, myCurrentMap)
							);
						} catch (Exception e) {
							// ignore
						}
					}
					myState = State.EXPECT_SELECTOR;
				} else {
					myName = token;
					myState = State.EXPECT_VALUE;
				}
				break;
			case EXPECT_VALUE:
				if (myCurrentMap != null && myName != null) {
					myCurrentMap.put(myName, token);
				}
				myState = State.EXPECT_NAME;
				break;
		}
	}
}
