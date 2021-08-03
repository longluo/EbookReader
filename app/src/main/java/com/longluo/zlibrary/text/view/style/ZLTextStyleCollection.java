package com.longluo.zlibrary.text.view.style;

import java.util.*;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.longluo.zlibrary.core.filesystem.ZLResourceFile;
import com.longluo.zlibrary.core.util.XmlUtil;
import com.longluo.zlibrary.text.model.ZLTextAlignmentType;

public class ZLTextStyleCollection {
	public final String Screen;
	private ZLTextBaseStyle myBaseStyle;
	private final List<ZLTextNGStyleDescription> myDescriptionList;
	private final ZLTextNGStyleDescription[] myDescriptionMap = new ZLTextNGStyleDescription[256];

	public ZLTextStyleCollection(String screen) {
		Screen = screen;
		final Map<Integer,ZLTextNGStyleDescription> descriptions =
			new SimpleCSSReader().read(ZLResourceFile.createResourceFile("default/styles.css"));
		myDescriptionList = Collections.unmodifiableList(
			new ArrayList<ZLTextNGStyleDescription>(descriptions.values())
		);
		for (Map.Entry<Integer,ZLTextNGStyleDescription> entry : descriptions.entrySet()) {
			myDescriptionMap[entry.getKey() & 0xFF] = entry.getValue();
		}
		XmlUtil.parseQuietly(
			ZLResourceFile.createResourceFile("default/styles.xml"),
			new TextStyleReader()
		);
	}

	public ZLTextBaseStyle getBaseStyle() {
		return myBaseStyle;
	}

	public List<ZLTextNGStyleDescription> getDescriptionList() {
		return myDescriptionList;
	}

	public ZLTextNGStyleDescription getDescription(byte kind) {
		return myDescriptionMap[kind & 0xFF];
	}

	private class TextStyleReader extends DefaultHandler {
		private int intValue(Attributes attributes, String name, int defaultValue) {
			final String value = attributes.getValue(name);
			if (value != null) {
				try {
					return Integer.parseInt(value);
				} catch (NumberFormatException e) {
				}
			}
			return defaultValue;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			if ("base".equals(localName) && Screen.equals(attributes.getValue("screen"))) {
				myBaseStyle = new ZLTextBaseStyle(
					Screen,
					attributes.getValue("family"),
					intValue(attributes, "fontSize", 0)
				);
			}
		}
	}
}
