package com.longluo.ebookreader.network;

import java.util.Map;
import java.util.List;

import com.longluo.ebookreader.network.urlInfo.*;
import com.longluo.ebookreader.network.opds.OPDSCustomNetworkLink;
import com.longluo.ebookreader.network.opds.OPDSPredefinedNetworkLink;

public abstract class NetworkDatabase {
	private static NetworkDatabase ourInstance;

	public static NetworkDatabase Instance() {
		return ourInstance;
	}

	private final NetworkLibrary myLibrary;

	protected NetworkDatabase(NetworkLibrary library) {
		myLibrary = library;
		ourInstance = this;
	}

	protected abstract void executeAsTransaction(Runnable actions);

	protected INetworkLink createLink(int id, INetworkLink.Type type, String predefinedId, String title, String summary, String language, UrlInfoCollection<UrlInfoWithDate> infos) {
		if (title == null || infos.getInfo(UrlInfo.Type.Catalog) == null) {
			return null;
		}
		switch (type) {
			default:
				return new OPDSCustomNetworkLink(
					myLibrary, id, type, title, summary, language, infos
				);
			case Predefined:
				return new OPDSPredefinedNetworkLink(
					myLibrary, id, predefinedId, title, summary, language, infos
				);
		}
	}

	protected abstract List<INetworkLink> listLinks();
	protected abstract void saveLink(INetworkLink link);
	protected abstract void deleteLink(INetworkLink link);

	protected abstract Map<String,String> getLinkExtras(INetworkLink link);
	protected abstract void setLinkExtras(INetworkLink link, Map<String,String> extras);
}
