package com.longluo.ebookreader.fbreader;

import com.longluo.zlibrary.core.options.ZLIntegerRangeOption;

class ChangeFontSizeAction extends FBAction {
	private final int myDelta;

	ChangeFontSizeAction(FBReaderApp fbreader, int delta) {
		super(fbreader);
		myDelta = delta;
	}

	@Override
	protected void run(Object ... params) {
		final ZLIntegerRangeOption option =
			Reader.ViewOptions.getTextStyleCollection().getBaseStyle().FontSizeOption;
		option.setValue(option.getValue() + myDelta);
		Reader.clearTextCaches();
		Reader.getViewWidget().repaint();
	}
}
