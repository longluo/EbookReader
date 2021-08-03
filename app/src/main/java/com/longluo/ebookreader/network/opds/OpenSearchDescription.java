package com.longluo.ebookreader.network.opds;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.longluo.zlibrary.core.util.MimeType;

class OpenSearchDescription {
	public static OpenSearchDescription createDefault(String tmpl, MimeType mime) {
		return new OpenSearchDescription(tmpl, -1, -1, mime);
	}

	public final String Template;
	public final int IndexOffset;
	public final int PageOffset;
	public final MimeType Mime;

	public final int ItemsPerPage = 20;

	OpenSearchDescription(String tmpl, int indexOffset, int pageOffset, MimeType mime) {
		Template = tmpl;
		IndexOffset = indexOffset;
		PageOffset = pageOffset;
		Mime = mime;
	}

	public boolean isValid() {
		return makeQuery("") != null;
	}

	// searchTerms -- an HTML-encoded string
	public String makeQuery(String searchTerms) {
		final StringBuffer query = new StringBuffer();
		final Matcher m = Pattern.compile("\\{([^}]*)\\}").matcher(Template);
		while (m.find()) {
			String name = m.group(1);
			if (name == null || name.length() == 0 || name.contains(":")) {
				return null;
			}
			final boolean optional = name.endsWith("?");
			if (optional) {
				name = name.substring(0, name.length() - 1);
			}
			name = name.intern();
			if (name == "searchTerms") {
				m.appendReplacement(query, searchTerms);
			} else if (name == "count") {
				m.appendReplacement(query, String.valueOf(ItemsPerPage));
			} else if (optional) {
				m.appendReplacement(query, "");
			} else if (name == "startIndex") {
				if (IndexOffset > 0) {
					m.appendReplacement(query, String.valueOf(IndexOffset));
				} else {
					return null;
				}
			} else if (name == "startPage") {
				if (PageOffset > 0) {
					m.appendReplacement(query, String.valueOf(PageOffset));
				} else {
					return null;
				}
			} else if (name == "language") {
				m.appendReplacement(query, "*");
			} else if (name == "inputEncoding" || name == "outputEncoding") {
				m.appendReplacement(query, "UTF-8");
			} else {
				return null;
			}
		}
		m.appendTail(query);
		return query.toString();
	}
}