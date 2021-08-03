package com.longluo.android.ebookreader;

import android.content.Intent;

import com.longluo.ebookreader.bookmodel.BookModel;
import com.longluo.ebookreader.fbreader.FBReaderApp;

import com.longluo.android.util.OrientationUtil;

class ShowTOCAction extends FBAndroidAction {
	ShowTOCAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	static boolean isTOCAvailable(FBReaderApp reader) {
		if (reader == null) {
			return false;
		}
		final BookModel model = reader.Model;
		return model != null && model.TOCTree.hasChildren();
	}

	@Override
	public boolean isVisible() {
		return isTOCAvailable(Reader);
	}

	@Override
	protected void run(Object ... params) {
		OrientationUtil.startActivity(
			BaseActivity, new Intent(BaseActivity.getApplicationContext(), TOCActivity.class)
		);
	}
}
