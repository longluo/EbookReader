package com.longluo.android.ebookreader;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import com.longluo.ebookreader.fbreader.FBReaderApp;

import com.longluo.android.ebookreader.api.FBReaderIntents;
import com.longluo.android.ebookreader.bookmark.BookmarksActivity;
import com.longluo.android.util.OrientationUtil;
import com.longluo.android.util.PackageUtil;

class ShowBookmarksAction extends FBAndroidAction {
	ShowBookmarksAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	public boolean isVisible() {
		return Reader.Model != null;
	}

	@Override
	protected void run(Object ... params) {
		final Intent externalIntent =
			new Intent(FBReaderIntents.Action.EXTERNAL_BOOKMARKS);
		final Intent internalIntent =
			new Intent(BaseActivity.getApplicationContext(), BookmarksActivity.class);
		if (PackageUtil.canBeStarted(BaseActivity, externalIntent, true)) {
			try {
				startBookmarksActivity(externalIntent);
			} catch (ActivityNotFoundException e) {
				startBookmarksActivity(internalIntent);
			}
		} else {
			startBookmarksActivity(internalIntent);
		}
	}

	private void startBookmarksActivity(Intent intent) {
		FBReaderIntents.putBookExtra(intent, Reader.getCurrentBook());
		FBReaderIntents.putBookmarkExtra(intent, Reader.createBookmark(80, true));
		OrientationUtil.startActivity(BaseActivity, intent);
	}
}
