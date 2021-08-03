package com.longluo.android.ebookreader.bookmark;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Window;

import com.longluo.zlibrary.core.resources.ZLResource;
import com.longluo.zlibrary.core.util.ZLColor;

import com.longluo.ebookreader.book.BookmarkUtil;
import com.longluo.ebookreader.book.HighlightingStyle;

import com.longluo.android.ebookreader.libraryService.BookCollectionShadow;
import com.longluo.android.ebookreader.preferences.*;

public class EditStyleActivity extends PreferenceActivity {
	static final String STYLE_ID_KEY = "style.id";

	private final ZLResource myRootResource = ZLResource.resource("editStyle");
	private final BookCollectionShadow myCollection = new BookCollectionShadow();
	private HighlightingStyle myStyle;
	private BgColorPreference myBgColorPreference;

	@Override
	protected void onCreate(Bundle bundle) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(bundle);
		Thread.setDefaultUncaughtExceptionHandler(new com.longluo.zlibrary.ui.android.library.UncaughtExceptionHandler(this));

		final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(this);
		setPreferenceScreen(screen);

		myCollection.bindToService(this, new Runnable() {
			public void run() {
				myStyle = myCollection.getHighlightingStyle(getIntent().getIntExtra(STYLE_ID_KEY, -1));
				if (myStyle == null) {
					finish();
					return;
				}
				screen.addPreference(new NamePreference());
				screen.addPreference(new InvisiblePreference());
				myBgColorPreference = new BgColorPreference();
				screen.addPreference(myBgColorPreference);
			}
		});
	}

	@Override
	protected void onDestroy() {
		myCollection.unbind();

		super.onDestroy();
	}

	private class NamePreference extends ZLStringPreference {
		NamePreference() {
			super(EditStyleActivity.this, myRootResource, "name");
			super.setValue(BookmarkUtil.getStyleName(myStyle));
		}

		@Override
		protected void setValue(String value) {
			super.setValue(value);
			BookmarkUtil.setStyleName(myStyle, value);
			myCollection.saveHighlightingStyle(myStyle);
		}
	}

	private class InvisiblePreference extends ZLCheckBoxPreference {
		private ZLColor mySavedBgColor;

		InvisiblePreference() {
			super(EditStyleActivity.this, myRootResource.getResource("invisible"));
			setChecked(myStyle.getBackgroundColor() == null);
		}

		@Override
		protected void onClick() {
			super.onClick();
			if (isChecked()) {
				mySavedBgColor = myStyle.getBackgroundColor();
				myStyle.setBackgroundColor(null);
				myBgColorPreference.setEnabled(false);
			} else {
				myStyle.setBackgroundColor(
					mySavedBgColor != null ? mySavedBgColor : new ZLColor(127, 127, 127)
				);
				myBgColorPreference.setEnabled(true);
			}
			myCollection.saveHighlightingStyle(myStyle);
		}
	}

	private class BgColorPreference extends ColorPreference {
		BgColorPreference() {
			super(EditStyleActivity.this);
			setEnabled(getSavedColor() != null);
		}

		@Override
		public String getTitle() {
			return myRootResource.getResource("bgColor").getValue();
		}

		@Override
		protected ZLColor getSavedColor() {
			return myStyle.getBackgroundColor();
		}

		@Override
		protected void saveColor(ZLColor color) {
			myStyle.setBackgroundColor(color);
			myCollection.saveHighlightingStyle(myStyle);
		}
	}
}
