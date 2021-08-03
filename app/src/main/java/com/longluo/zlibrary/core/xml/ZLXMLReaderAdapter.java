package com.longluo.zlibrary.core.xml;

import java.io.*;
import java.util.*;

import com.longluo.zlibrary.core.filesystem.ZLFile;

public abstract class ZLXMLReaderAdapter implements ZLXMLReader {
	private Map<String,String> myNamespaceMap = Collections.emptyMap();

	public boolean readQuietly(ZLFile file) {
		try {
			read(file);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

 	public boolean readQuietly(String string) {
		try {
			read(string);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

 	public void read(ZLFile file) throws IOException {
		ZLXMLProcessor.read(this, file);
	}

	public void read(InputStream stream) throws IOException {
		ZLXMLProcessor.read(this, stream, 65536);
	}

	public void read(String string) throws IOException {
		ZLXMLProcessor.read(this, new StringReader(string), 65536);
	}

	public void read(Reader reader) throws IOException {
		ZLXMLProcessor.read(this, reader, 65536);
	}

	public boolean dontCacheAttributeValues() {
		return false;
	}

	public boolean startElementHandler(String tag, ZLStringMap attributes) {
		return false;
	}

	public boolean endElementHandler(String tag) {
		return false;
	}

	public void characterDataHandler(char[] ch, int start, int length) {
	}

	public void characterDataHandlerFinal(char[] ch, int start, int length) {
		characterDataHandler(ch, start, length);
	}

	public void startDocumentHandler() {
	}

	public void endDocumentHandler() {
	}

	public boolean processNamespaces() {
		return false;
	}

	public void namespaceMapChangedHandler(Map<String,String> namespaces) {
		myNamespaceMap = namespaces != null ? namespaces : Collections.<String,String>emptyMap();
	}

	public boolean testTag(String namespace, String name, String tag) {
		if (name.equals(tag) && namespace.equals(myNamespaceMap.get(""))) {
			return true;
		}
		final int nameLen = name.length();
		final int tagLen = tag.length();
		if (tagLen < nameLen + 2) {
			return false;
		}
		if (tag.endsWith(name) && tag.charAt(tagLen - nameLen - 1) == ':') {
			return namespace.equals(myNamespaceMap.get(tag.substring(0, tagLen - nameLen - 1)));
		}
		return false;
	}

	public String getAttributeValue(ZLStringMap attributes, String namespace, String name) {
		if (namespace == null) {
			return attributes.getValue(name);
		}

		final int size = attributes.getSize();
		if (size == 0) {
			return null;
		}
		final String postfix = ":" + name;
		for (int i = size - 1; i >= 0; --i) {
			final String key = attributes.getKey(i);
			if (key.endsWith(postfix)) {
				final String nsKey = key.substring(0, key.length() - postfix.length());
				if (namespace.equals(myNamespaceMap.get(nsKey))) {
					return attributes.getValue(i);
				}
			}
		}
		return null;
	}

	interface Predicate {
		boolean accepts(String namespace);
	}

	protected String getAttributeValue(ZLStringMap attributes, Predicate predicate, String name) {
		final int size = attributes.getSize();
		if (size == 0) {
			return null;
		}
		final String postfix = ":" + name;
		for (int i = size - 1; i >= 0; --i) {
			final String key = attributes.getKey(i);
			if (key.endsWith(postfix)) {
				final String ns =
					myNamespaceMap.get(key.substring(0, key.length() - postfix.length()));
				if (ns != null && predicate.accepts(ns)) {
					return attributes.getValue(i);
				}
			}
		}
		return null;
	}

	public void collectExternalEntities(HashMap<String,char[]> entityMap) {
	}

	public List<String> externalDTDs() {
		return Collections.emptyList();
	}
}
