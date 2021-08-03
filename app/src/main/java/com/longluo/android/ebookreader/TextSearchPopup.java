package com.longluo.android.ebookreader;

import android.view.View;
import android.widget.RelativeLayout;

import com.longluo.ebookreader.R;

import com.longluo.zlibrary.core.resources.ZLResource;
import com.longluo.ebookreader.fbreader.ActionCode;
import com.longluo.ebookreader.fbreader.FBReaderApp;

final class TextSearchPopup extends PopupPanel implements View.OnClickListener {
	final static String ID = "TextSearchPopup";

	TextSearchPopup(FBReaderApp fbReader) {
		super(fbReader);
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	protected void hide_() {
		getReader().getTextView().clearFindResults();
		super.hide_();
	}

	@Override
	public synchronized void createControlPanel(FBReader activity, RelativeLayout root) {
		if (myWindow != null && activity == myWindow.getContext()) {
			return;
		}

		activity.getLayoutInflater().inflate(R.layout.search_panel, root);
		myWindow = (SimplePopupWindow)root.findViewById(R.id.search_panel);

		final ZLResource resource = ZLResource.resource("textSearchPopup");
		setupButton(R.id.search_panel_previous, resource.getResource("findPrevious").getValue());
		setupButton(R.id.search_panel_next, resource.getResource("findNext").getValue());
		setupButton(R.id.search_panel_close, resource.getResource("close").getValue());
	}

	private void setupButton(int buttonId, String description) {
		final View button = myWindow.findViewById(buttonId);
		button.setOnClickListener(this);
		button.setContentDescription(description);
	}

	@Override
	protected synchronized void update() {
		if (myWindow == null) {
			return;
		}

		myWindow.findViewById(R.id.search_panel_previous).setEnabled(
			Application.isActionEnabled(ActionCode.FIND_PREVIOUS)
		);
		myWindow.findViewById(R.id.search_panel_next).setEnabled(
			Application.isActionEnabled(ActionCode.FIND_NEXT)
		);
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.search_panel_previous:
				Application.runAction(ActionCode.FIND_PREVIOUS);
				break;
			case R.id.search_panel_next:
				Application.runAction(ActionCode.FIND_NEXT);
				break;
			case R.id.search_panel_close:
				Application.runAction(ActionCode.CLEAR_FIND_RESULTS);
				storePosition();
				StartPosition = null;
				Application.hideActivePopup();
		}
	}
}
