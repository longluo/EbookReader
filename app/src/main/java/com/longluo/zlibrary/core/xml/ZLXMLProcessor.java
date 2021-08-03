package com.longluo.zlibrary.core.xml;

import java.util.*;
import java.io.*;

import com.longluo.zlibrary.core.filesystem.ZLFile;

public abstract class ZLXMLProcessor {
	public static Map<String,char[]> getEntityMap(List<String> dtdList) {
		try {
			return ZLXMLParser.getDTDMap(dtdList);
		} catch (IOException e) {
			return Collections.emptyMap();
		}
	}

	public static void read(ZLXMLReader xmlReader, InputStream stream, int bufferSize) throws IOException {
		ZLXMLParser parser = null;
		try {
			parser = new ZLXMLParser(xmlReader, stream, bufferSize);
			xmlReader.startDocumentHandler();
			parser.doIt();
			xmlReader.endDocumentHandler();
		} finally {
			if (parser != null) {
				parser.finish();
			}
		}
	}

	public static void read(ZLXMLReader xmlReader, Reader reader, int bufferSize) throws IOException {
		ZLXMLParser parser = null;
		try {
			parser = new ZLXMLParser(xmlReader, reader, bufferSize);
			xmlReader.startDocumentHandler();
			parser.doIt();
			xmlReader.endDocumentHandler();
		} finally {
			if (parser != null) {
				parser.finish();
			}
		}
	}

	public static void read(ZLXMLReader xmlReader, ZLFile file) throws IOException {
		read(xmlReader, file, 65536);
	}

	public static void read(ZLXMLReader xmlReader, ZLFile file, int bufferSize) throws IOException {
		InputStream stream = file.getInputStream();
		try {
			read(xmlReader, stream, bufferSize);
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
			}
		}
	}
}
