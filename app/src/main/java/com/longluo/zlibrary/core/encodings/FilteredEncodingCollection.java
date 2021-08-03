package com.longluo.zlibrary.core.encodings;

import java.util.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.util.Xml;

import com.longluo.zlibrary.core.filesystem.ZLResourceFile;

abstract class FilteredEncodingCollection extends EncodingCollection {
	private final List<Encoding> myEncodings = new ArrayList<Encoding>();
	private final Map<String,Encoding> myEncodingByAlias = new HashMap<String,Encoding>();

	FilteredEncodingCollection() {
		try {
			final ZLResourceFile file = ZLResourceFile.createResourceFile("encodings/Encodings.xml");
			Xml.parse(file.getInputStream(), Xml.Encoding.UTF_8, new EncodingCollectionReader());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract boolean isEncodingSupported(String name);

	@Override
	public List<Encoding> encodings() {
		return Collections.unmodifiableList(myEncodings);
	}

	@Override
	public Encoding getEncoding(String alias) {
		Encoding e = myEncodingByAlias.get(alias);
		if (e == null && isEncodingSupported(alias)) {
			e = new Encoding(null, alias, alias);
			myEncodingByAlias.put(alias, e);
			myEncodings.add(e);
		}
		return e;
	}

	@Override
	public Encoding getEncoding(int code) {
		return getEncoding(String.valueOf(code));
	}

	public boolean providesConverterFor(String alias) {
		return myEncodingByAlias.containsKey(alias) || isEncodingSupported(alias);
	}

	private class EncodingCollectionReader extends DefaultHandler {
		private String myCurrentFamilyName;
		private Encoding myCurrentEncoding;

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if ("group".equals(localName)) {
				myCurrentFamilyName = attributes.getValue("name");
			} else if ("encoding".equals(localName)) {
				final String name = attributes.getValue("name").toLowerCase();
				final String region = attributes.getValue("region");
				if (isEncodingSupported(name)) {
					myCurrentEncoding = new Encoding(
						myCurrentFamilyName, name, name + " (" + region + ")"
					);
					myEncodings.add(myCurrentEncoding);
					myEncodingByAlias.put(name, myCurrentEncoding);
				} else {
					myCurrentEncoding = null;
				}
			} else if ("code".equals(localName)) {
				if (myCurrentEncoding != null) {
					myEncodingByAlias.put(attributes.getValue("number"), myCurrentEncoding);
				}
			} else if ("alias".equals(localName)) {
				if (myCurrentEncoding != null) {
					myEncodingByAlias.put(attributes.getValue("name").toLowerCase(), myCurrentEncoding);
				}
			}
		}
	}
}
