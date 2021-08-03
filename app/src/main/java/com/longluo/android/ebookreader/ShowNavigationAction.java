package com.longluo.android.ebookreader;

import com.longluo.zlibrary.text.model.ZLTextModel;
import com.longluo.zlibrary.text.view.ZLTextView;

import com.longluo.ebookreader.fbreader.FBReaderApp;

class ShowNavigationAction extends FBAndroidAction {
	ShowNavigationAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	public boolean isVisible() {
		final ZLTextView view = (ZLTextView)Reader.getCurrentView();
		final ZLTextModel textModel = view.getModel();
		return textModel != null && textModel.getParagraphsNumber() != 0;
	}

	@Override
	protected void run(Object ... params) {
		BaseActivity.navigate();
	}
}
