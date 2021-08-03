package com.longluo.android.ebookreader.library;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

public class LibrarySearchActivity extends Activity {
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			final String pattern = intent.getStringExtra(SearchManager.QUERY);
			if (pattern != null && pattern.length() > 0) {
				intent = new Intent(
					LibraryActivity.START_SEARCH_ACTION, null, this, LibraryActivity.class
				);
				intent.putExtra(SearchManager.QUERY, pattern);
				startActivity(intent);
			}
		}
		finish();
	}
}
