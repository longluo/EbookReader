package com.longluo.android.ebookreader;

import android.view.View;
import android.widget.RelativeLayout;

import com.longluo.zlibrary.core.resources.ZLResource;
import com.longluo.ebookreader.fbreader.ActionCode;
import com.longluo.ebookreader.fbreader.FBReaderApp;
import com.longluo.ebookreader.R;

class SelectionPopup extends PopupPanel implements View.OnClickListener {
	final static String ID = "SelectionPopup";

	SelectionPopup(FBReaderApp fbReader) {
		super(fbReader);
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void createControlPanel(FBReader activity, RelativeLayout root) {
		if (myWindow != null && activity == myWindow.getContext()) {
			return;
		}

		activity.getLayoutInflater().inflate(R.layout.selection_panel, root);
		myWindow = (SimplePopupWindow)root.findViewById(R.id.selection_panel);

		final ZLResource resource = ZLResource.resource("selectionPopup");
		setupButton(R.id.selection_panel_copy, resource.getResource("copyToClipboard").getValue());
		setupButton(R.id.selection_panel_share, resource.getResource("share").getValue());
		setupButton(R.id.selection_panel_translate, resource.getResource("translate").getValue());
		setupButton(R.id.selection_panel_bookmark, resource.getResource("bookmark").getValue());
		setupButton(R.id.selection_panel_close, resource.getResource("close").getValue());
	}

	private void setupButton(int buttonId, String description) {
		final View button = myWindow.findViewById(buttonId);
		button.setOnClickListener(this);
		button.setContentDescription(description);
	}

	public void move(int selectionStartY, int selectionEndY) {
		if (myWindow == null) {
			return;
		}

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.WRAP_CONTENT,
			RelativeLayout.LayoutParams.WRAP_CONTENT
		);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

		final int verticalPosition;
		final int screenHeight = ((View)myWindow.getParent()).getHeight();
		final int diffTop = screenHeight - selectionEndY;
		final int diffBottom = selectionStartY;
		if (diffTop > diffBottom) {
			verticalPosition = diffTop > myWindow.getHeight() + 20
				? RelativeLayout.ALIGN_PARENT_BOTTOM : RelativeLayout.CENTER_VERTICAL;
		} else {
			verticalPosition = diffBottom > myWindow.getHeight() + 20
				? RelativeLayout.ALIGN_PARENT_TOP : RelativeLayout.CENTER_VERTICAL;
		}

		layoutParams.addRule(verticalPosition);
		myWindow.setLayoutParams(layoutParams);
	}

	@Override
	protected void update() {
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.selection_panel_copy:
				Application.runAction(ActionCode.SELECTION_COPY_TO_CLIPBOARD);
				break;
			case R.id.selection_panel_share:
				Application.runAction(ActionCode.SELECTION_SHARE);
				break;
			case R.id.selection_panel_translate:
				Application.runAction(ActionCode.SELECTION_TRANSLATE);
				break;
			case R.id.selection_panel_bookmark:
				Application.runAction(ActionCode.SELECTION_BOOKMARK);
				break;
			case R.id.selection_panel_close:
				Application.runAction(ActionCode.SELECTION_CLEAR);
				break;
		}
		Application.hideActivePopup();
	}
}
