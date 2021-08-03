package com.longluo.android.ebookreader;

import android.content.Intent;
import android.os.Parcelable;
import android.view.View;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.OnClickWrapper;

import com.longluo.zlibrary.core.resources.ZLResource;

import com.longluo.ebookreader.book.Bookmark;
import com.longluo.ebookreader.fbreader.FBReaderApp;

import com.longluo.android.ebookreader.api.FBReaderIntents;
import com.longluo.android.ebookreader.bookmark.EditBookmarkActivity;
import com.longluo.android.util.OrientationUtil;

public class SelectionBookmarkAction extends FBAndroidAction {
	SelectionBookmarkAction(FBReader baseApplication, FBReaderApp fbreader) {
		super(baseApplication, fbreader);
	}

	@Override
	protected void run(Object ... params) {
		final Bookmark bookmark;
		if (params.length != 0) {
			bookmark = (Bookmark)params[0];
		} else {
			bookmark = Reader.addSelectionBookmark();
		}
		if (bookmark == null) {
			return;
		}

		final SuperActivityToast toast =
			new SuperActivityToast(BaseActivity, SuperToast.Type.BUTTON);
		toast.setText(bookmark.getText());
		toast.setDuration(SuperToast.Duration.EXTRA_LONG);
		toast.setButtonIcon(
			android.R.drawable.ic_menu_edit,
			ZLResource.resource("dialog").getResource("button").getResource("edit").getValue()
		);
		toast.setOnClickWrapper(new OnClickWrapper("bkmk", new SuperToast.OnClickListener() {
			@Override
			public void onClick(View view, Parcelable token) {
				final Intent intent =
					new Intent(BaseActivity.getApplicationContext(), EditBookmarkActivity.class);
				FBReaderIntents.putBookmarkExtra(intent, bookmark);
				OrientationUtil.startActivity(BaseActivity, intent);
			}
		}));
		BaseActivity.showToast(toast);
	}
}
