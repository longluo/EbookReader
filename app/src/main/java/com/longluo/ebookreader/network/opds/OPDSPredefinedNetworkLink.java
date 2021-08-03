package com.longluo.ebookreader.network.opds;

import com.longluo.ebookreader.network.IPredefinedNetworkLink;
import com.longluo.ebookreader.network.NetworkLibrary;
import com.longluo.ebookreader.network.urlInfo.*;

public class OPDSPredefinedNetworkLink extends OPDSNetworkLink implements IPredefinedNetworkLink {
	private static final String ID_PREFIX = "urn:fbreader-org-catalog:";

	private final String myPredefinedId;

	public OPDSPredefinedNetworkLink(NetworkLibrary library, int id, String predefinedId, String title, String summary, String language, UrlInfoCollection<UrlInfoWithDate> infos) {
		super(library, id, title, summary, language, infos);
		myPredefinedId = predefinedId;
	}

	public Type getType() {
		return Type.Predefined;
	}

	public String getPredefinedId() {
		return myPredefinedId;
	}

	@Override
	public String getShortName() {
		if (myPredefinedId.startsWith(ID_PREFIX)) {
			return myPredefinedId.substring(ID_PREFIX.length());
		}
		return myPredefinedId;
	}

	@Override
	public String getStringId() {
		return getShortName();
	}

	public boolean servesHost(String hostname) {
		return hostname != null && hostname.indexOf(getShortName()) != -1;
	}
}
