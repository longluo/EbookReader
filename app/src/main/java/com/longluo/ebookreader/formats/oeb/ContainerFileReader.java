package com.longluo.ebookreader.formats.oeb;

import com.longluo.zlibrary.core.xml.*;

class ContainerFileReader extends ZLXMLReaderAdapter {
	private String myRootPath;

	public String getRootPath() {
		return myRootPath;
	}

	@Override
	public boolean startElementHandler(String tag, ZLStringMap xmlattributes) {
		if ("rootfile".equalsIgnoreCase(tag)) {
			myRootPath = xmlattributes.getValue("full-path");
			if (myRootPath != null) {
				return true;
			}
		}
		return false;
	}
}
