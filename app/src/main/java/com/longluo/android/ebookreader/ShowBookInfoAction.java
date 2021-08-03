package com.longluo.android.ebookreader;

import android.content.Intent;

import com.longluo.ebookreader.fbreader.FBReaderApp;

import com.longluo.android.ebookreader.api.FBReaderIntents;
import com.longluo.android.ebookreader.library.BookInfoActivity;
import com.longluo.android.util.OrientationUtil;

class ShowBookInfoAction extends FBAndroidAction {
	ShowBookInfoAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	public boolean isVisible() {
		return Reader.Model != null;
	}

	@Override
	protected void run(Object ... params) {
		final Intent intent =
			new Intent(BaseActivity.getApplicationContext(), BookInfoActivity.class)
				.putExtra(BookInfoActivity.FROM_READING_MODE_KEY, true);
		FBReaderIntents.putBookExtra(intent, Reader.getCurrentBook());
		OrientationUtil.startActivity(BaseActivity, intent);
	}
}
