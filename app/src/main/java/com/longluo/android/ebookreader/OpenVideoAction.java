package com.longluo.android.ebookreader;

import java.util.Map;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.longluo.zlibrary.core.util.MimeType;
import com.longluo.zlibrary.text.view.ZLTextVideoElement;
import com.longluo.zlibrary.text.view.ZLTextVideoRegionSoul;

import com.longluo.ebookreader.fbreader.FBReaderApp;

import com.longluo.android.ebookreader.httpd.DataUtil;
import com.longluo.android.util.UIMessageUtil;

class OpenVideoAction extends FBAndroidAction {
	OpenVideoAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	protected void run(Object ... params) {
		if (params.length != 1 || !(params[0] instanceof ZLTextVideoRegionSoul)) {
			return;
		}

		final ZLTextVideoElement element = ((ZLTextVideoRegionSoul)params[0]).VideoElement;
		boolean playerNotFound = false;
		for (MimeType mimeType : MimeType.TYPES_VIDEO) {
			final String mime = mimeType.toString();
			final String path = element.Sources.get(mime);
			if (path == null) {
				continue;
			}
			final Intent intent = new Intent(Intent.ACTION_VIEW);
			final String url = DataUtil.buildUrl(BaseActivity.DataConnection, mime, path);
			if (url == null) {
				UIMessageUtil.showErrorMessage(BaseActivity, "videoServiceNotWorking");
				return;
			}
			intent.setDataAndType(Uri.parse(url), mime);
			try {
				BaseActivity.startActivity(intent);
				return;
			} catch (ActivityNotFoundException e) {
				playerNotFound = true;
				continue;
			}
		}
		if (playerNotFound) {
			UIMessageUtil.showErrorMessage(BaseActivity, "videoPlayerNotFound");
		}
	}
}
