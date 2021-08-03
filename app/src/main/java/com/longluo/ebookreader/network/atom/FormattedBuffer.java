package com.longluo.ebookreader.network.atom;

import com.longluo.zlibrary.core.xml.ZLStringMap;

import com.longluo.ebookreader.network.NetworkLibrary;
import com.longluo.ebookreader.network.HtmlUtil;

public class FormattedBuffer {
	public static enum Type {
		Text,
		Html,
		XHtml
	};

	private final NetworkLibrary myLibrary;
	private Type myType;
	private StringBuilder myBuffer = new StringBuilder();

	public FormattedBuffer(NetworkLibrary library, Type type) {
		myLibrary = library;
		myType = type;
	}

	public FormattedBuffer(NetworkLibrary library) {
		this(library, Type.Text);
	}

	public void appendText(CharSequence text) {
		if (text != null) {
			myBuffer.append(text);
		}
	}

	public void appendText(char[] data, int start, int length) {
		myBuffer.append(data, start, length);
	}

	public void appendStartTag(String tag, ZLStringMap attributes) {
		myBuffer.append("<").append(tag);
		for (int i = 0; i < attributes.getSize(); ++i) {
			final String key = attributes.getKey(i);
			final String value = attributes.getValue(key);
			myBuffer.append(" ").append(key).append("=\"");
			if (value != null) {
				myBuffer.append(value);
			}
			myBuffer.append("\"");
		}
		myBuffer.append(">");
	}

	public void appendEndTag(String tag) {
		myBuffer.append("</").append(tag).append(">");
	}

	public void reset(Type type) {
		myType = type;
		reset();
	}

	public void reset() {
		myBuffer.delete(0, myBuffer.length());
	}

	public CharSequence getText() {
		final String text = myBuffer.toString();

		switch (myType) {
			case Html:
			case XHtml:
				return HtmlUtil.getHtmlText(myLibrary, text);
			default:
				return text;
		}
	}

	@Override
	public String toString() {
		return myBuffer.toString();
	}
}
