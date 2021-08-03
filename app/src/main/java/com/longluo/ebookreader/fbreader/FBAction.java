package com.longluo.ebookreader.fbreader;

import com.longluo.zlibrary.core.application.ZLApplication;

public abstract class FBAction extends ZLApplication.ZLAction {
	protected final FBReaderApp Reader;

	public FBAction(FBReaderApp fbreader) {
		Reader = fbreader;
	}
}
