package com.longluo.ebookreader.network.authentication.litres;

import com.longluo.zlibrary.core.xml.ZLXMLReaderAdapter;
import com.longluo.zlibrary.core.network.ZLNetworkException;

class LitResAuthenticationXMLReader extends ZLXMLReaderAdapter {
	private ZLNetworkException myException;

	protected void setException(ZLNetworkException e) {
		myException = e;
	}

	public ZLNetworkException getException() {
		return myException;
	}
}
