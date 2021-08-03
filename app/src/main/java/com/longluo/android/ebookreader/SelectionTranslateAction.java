package com.longluo.android.ebookreader;

import com.longluo.ebookreader.fbreader.*;
import com.longluo.ebookreader.util.TextSnippet;

import com.longluo.android.ebookreader.dict.DictionaryUtil;

public class SelectionTranslateAction extends FBAndroidAction {
	SelectionTranslateAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	protected void run(Object ... params) {
		final FBView fbview = Reader.getTextView();
		final DictionaryHighlighting dictionaryHilite = DictionaryHighlighting.get(fbview);
		final TextSnippet snippet = fbview.getSelectedSnippet();

		if (dictionaryHilite == null || snippet == null) {
			return;
		}

		DictionaryUtil.openTextInDictionary(
			BaseActivity,
			snippet.getText(),
			fbview.getCountOfSelectedWords() == 1,
			fbview.getSelectionStartY(),
			fbview.getSelectionEndY(),
			new Runnable() {
				public void run() {
					fbview.addHighlighting(dictionaryHilite);
					Reader.getViewWidget().repaint();
				}
			}
		);
		fbview.clearSelection();
	}
}
