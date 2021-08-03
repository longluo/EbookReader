package com.longluo.android.ebookreader;

import java.io.File;
import java.util.List;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.*;

import com.longluo.zlibrary.core.network.ZLNetworkException;
import com.longluo.zlibrary.core.network.QuietNetworkContext;
import com.longluo.zlibrary.core.resources.ZLResource;
import com.longluo.zlibrary.text.view.*;
import com.longluo.ebookreader.R;
import com.longluo.zlibrary.ui.android.image.ZLAndroidImageData;

import com.longluo.ebookreader.book.Book;
import com.longluo.ebookreader.fbreader.BookElement;
import com.longluo.ebookreader.fbreader.FBReaderApp;
import com.longluo.ebookreader.fbreader.options.ColorProfile;
import com.longluo.ebookreader.network.opds.OPDSBookItem;
import com.longluo.ebookreader.network.urlInfo.BookUrlInfo;
import com.longluo.ebookreader.network.urlInfo.UrlInfo;

import com.longluo.android.util.UIMessageUtil;
import com.longluo.android.util.UIUtil;

class DisplayBookPopupAction extends FBAndroidAction {
	DisplayBookPopupAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	private void openBook(final PopupWindow popup, final Book book) {
		if (book == null) {
			return;
		}

		BaseActivity.runOnUiThread(new Runnable() {
			public void run() {
				popup.dismiss();
				Reader.openBook(book, null, null, null);
			}
		});
	}

	@Override
	protected void run(Object ... params) {
		if (params.length != 1 || !(params[0] instanceof ZLTextRegion)) {
			return;
		}
		final ZLTextRegion region = (ZLTextRegion)params[0];
		if (!(region.getSoul() instanceof ExtensionRegionSoul)) {
			return;
		}
		final ExtensionElement e = ((ExtensionRegionSoul)region.getSoul()).Element;
		if (!(e instanceof BookElement)) {
			return;
		}
		final BookElement element = (BookElement)e;
		if (!element.isInitialized()) {
			return;
		}

		final View mainView = (View)BaseActivity.getViewWidget();
		final View bookView = BaseActivity.getLayoutInflater().inflate(
			ColorProfile.NIGHT.equals(Reader.ViewOptions.ColorProfileName.getValue())
				? R.layout.book_popup_night : R.layout.book_popup,
			null
		);
		final int inch = (int)TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_IN, 1, BaseActivity.getResources().getDisplayMetrics()
		);
		final PopupWindow popup = new PopupWindow(
			bookView,
			Math.min(4 * inch, mainView.getWidth() * 9 / 10),
			Math.min(3 * inch, mainView.getHeight() * 9 / 10)
		);
		popup.setFocusable(true);
		popup.setOutsideTouchable(true);

		final ImageView coverView = (ImageView)bookView.findViewById(R.id.book_popup_cover);
		if (coverView != null) {
			final ZLAndroidImageData imageData = (ZLAndroidImageData)element.getImageData();
			if (imageData != null) {
				coverView.setImageBitmap(imageData.getFullSizeBitmap());
			}
		}

		final OPDSBookItem item = element.getItem();

		final TextView headerView = (TextView)bookView.findViewById(R.id.book_popup_header_text);
		final StringBuilder text = new StringBuilder();
		for (OPDSBookItem.AuthorData author : item.Authors) {
			text.append("<p><i>").append(author.DisplayName).append("</i></p>");
		}
		text.append("<h3>").append(item.Title).append("</h3>");
		headerView.setText(Html.fromHtml(text.toString()));

		final TextView descriptionView = (TextView)bookView.findViewById(R.id.book_popup_description_text);
		descriptionView.setText(item.getSummary());
		descriptionView.setMovementMethod(new LinkMovementMethod());

		final ZLResource buttonResource = ZLResource.resource("dialog").getResource("button");
		final View buttonsView = bookView.findViewById(R.id.book_popup_buttons);

		final Button downloadButton = (Button)buttonsView.findViewById(R.id.ok_button);
		downloadButton.setText(buttonResource.getResource("download").getValue());
		final List<UrlInfo> infos = item.getAllInfos(UrlInfo.Type.Book);
		if (infos.isEmpty() || !(infos.get(0) instanceof BookUrlInfo)) {
			downloadButton.setEnabled(false);
		} else {
			final BookUrlInfo bookInfo = (BookUrlInfo)infos.get(0);
			final String fileName = bookInfo.makeBookFileName(UrlInfo.Type.Book);
			final Book book = Reader.Collection.getBookByFile(fileName);
			if (book != null) {
				downloadButton.setText(buttonResource.getResource("openBook").getValue());
				downloadButton.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						openBook(popup, book);
					}
				});
			} else {
				final File file = new File(fileName);
				if (file.exists()) {
					file.delete();
				}
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				downloadButton.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						UIUtil.wait(
							"downloadingBook", item.Title.toString(),
							new Runnable() {
								public void run() {
									try {
										new QuietNetworkContext().downloadToFile(bookInfo.Url, file);
										openBook(popup, Reader.Collection.getBookByFile(fileName));
									} catch (ZLNetworkException e) {
										UIMessageUtil.showErrorMessage(BaseActivity, "downloadFailed");
										e.printStackTrace();
									}
								}
							},
							BaseActivity
						);
					}
				});
			}
		}

		final Button cancelButton = (Button)buttonsView.findViewById(R.id.cancel_button);
		cancelButton.setText(buttonResource.getResource("cancel").getValue());
		cancelButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				popup.dismiss();
			}
		});

		downloadButton.setTextColor(0xFF777777);
		cancelButton.setTextColor(0xFF777777);

		popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
			public void onDismiss() {
			}
		});

		popup.showAtLocation(BaseActivity.getCurrentFocus(), Gravity.CENTER, 0, 0);
	}
}
