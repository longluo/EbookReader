package com.longluo.ebookreader.network.opds;

import java.util.List;

import com.longluo.zlibrary.core.util.MimeType;
import com.longluo.zlibrary.core.util.ZLNetworkUtil;

import com.longluo.ebookreader.network.atom.ATOMLink;

class OPDSCatalogInfoHandler extends AbstractOPDSFeedHandler {
	public boolean FeedStarted;
	public String Icon;
	public CharSequence Title;
	public CharSequence Summary;

	public OpenSearchDescription DirectOpenSearchDescription;
	private final List<String> myOpensearchDescriptionURLs;

	private final String myBaseURL;
	private final OPDSNetworkLink myLink;

	public OPDSCatalogInfoHandler(String baseUrl, OPDSNetworkLink link, List<String> opensearchDescriptionURLs) {
		myBaseURL = baseUrl;
		myLink = link;
		myOpensearchDescriptionURLs = opensearchDescriptionURLs;
	}

	public boolean processFeedMetadata(OPDSFeedMetadata feed, boolean beforeEntries) {
		Icon = feed.Icon != null ? ZLNetworkUtil.url(myBaseURL, feed.Icon.Uri) : null;
		Title = feed.Title;
		Summary = feed.Subtitle;

		for (ATOMLink link: feed.Links) {
			final MimeType mime = MimeType.get(link.getType());
			final String rel = myLink.relation(link.getRel(), mime);
			if ("search".equals(rel)) {
				if (MimeType.APP_OPENSEARCHDESCRIPTION.equals(mime)) {
					myOpensearchDescriptionURLs.add(ZLNetworkUtil.url(myBaseURL, link.getHref()));
				} else if (MimeType.APP_ATOM_XML.weakEquals(mime) || MimeType.TEXT_HTML.weakEquals(mime)) {
					final String tmpl = ZLNetworkUtil.url(myBaseURL, link.getHref());
					final OpenSearchDescription descr = OpenSearchDescription.createDefault(tmpl, mime);
					if (descr.isValid()) {
						DirectOpenSearchDescription = descr;
					}
				}
			}
		}
		return true;
	}

	public void processFeedStart() {
		FeedStarted = true;
	}

	public void processFeedEnd() {
	}

	public boolean processFeedEntry(OPDSEntry entry) {
		return true;
	}
}
