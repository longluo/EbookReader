package com.longluo.android.ebookreader;

import com.longluo.ebookreader.fbreader.FBReaderApp;

class SwitchProfileAction extends FBAndroidAction {
	private String myProfileName;

	SwitchProfileAction(FBReader baseActivity, FBReaderApp fbreader, String profileName) {
		super(baseActivity, fbreader);
		myProfileName = profileName;
	}

	@Override
	public boolean isVisible() {
		return !myProfileName.equals(Reader.ViewOptions.ColorProfileName.getValue());
	}

	@Override
	protected void run(Object ... params) {
		Reader.ViewOptions.ColorProfileName.setValue(myProfileName);
		Reader.getViewWidget().reset();
		Reader.getViewWidget().repaint();
	}
}
