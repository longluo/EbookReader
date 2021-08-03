package com.longluo.ebookreader.network;

import android.text.*;
import android.text.style.URLSpan;

public abstract class HtmlUtil {
	public static CharSequence getHtmlText(NetworkLibrary library, String text) {
		final Spanned htmlText = Html.fromHtml(text);
		if (htmlText.getSpans(0, htmlText.length(), URLSpan.class).length == 0) {
			return htmlText;
		}
		final Spannable newHtmlText = Spannable.Factory.getInstance().newSpannable(htmlText);
		for (URLSpan span : newHtmlText.getSpans(0, newHtmlText.length(), URLSpan.class)) {
			final int start = newHtmlText.getSpanStart(span);
			final int end = newHtmlText.getSpanEnd(span);
			final int flags = newHtmlText.getSpanFlags(span);
			final String url = library.rewriteUrl(span.getURL(), true);
			newHtmlText.removeSpan(span);
			newHtmlText.setSpan(new URLSpan(url), start, end, flags);
		}
		return newHtmlText;
	}
}
