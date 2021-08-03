package com.longluo.ebookreader.network.opds;

import com.longluo.zlibrary.core.network.ZLNetworkContext;
import com.longluo.zlibrary.core.network.QuietNetworkContext;
import com.longluo.zlibrary.core.resources.ZLResource;
import com.longluo.zlibrary.core.util.MimeType;

import com.longluo.ebookreader.fbreader.options.SyncOptions;
import com.longluo.ebookreader.network.ISyncNetworkLink;
import com.longluo.ebookreader.network.NetworkLibrary;
import com.longluo.ebookreader.network.sync.SyncUtil;
import com.longluo.ebookreader.network.urlInfo.*;

public class OPDSSyncNetworkLink extends OPDSNetworkLink implements ISyncNetworkLink {
	private static UrlInfoCollection<UrlInfoWithDate> initialUrlInfos() {
		final UrlInfoCollection<UrlInfoWithDate> infos = new UrlInfoCollection<UrlInfoWithDate>();
		infos.addInfo(new UrlInfoWithDate(
			UrlInfo.Type.Catalog,
			SyncOptions.OPDS_URL,
			MimeType.OPDS
		));
		infos.addInfo(new UrlInfoWithDate(
			UrlInfo.Type.Search,
			SyncOptions.BASE_URL + "opds/search/%s",
			MimeType.OPDS
		));
		infos.addInfo(new UrlInfoWithDate(
			UrlInfo.Type.Image,
			SyncOptions.BASE_URL + "static/images/logo-120x120.png",
			MimeType.IMAGE_PNG
		));
		infos.addInfo(new UrlInfoWithDate(
			UrlInfo.Type.SearchIcon,
			SyncOptions.BASE_URL + "static/images/folders-light/search.png",
			MimeType.IMAGE_PNG
		));
		return infos;
	}

	private static ZLResource resource() {
		return NetworkLibrary.resource().getResource("sync");
	}

	public OPDSSyncNetworkLink(NetworkLibrary library) {
		this(library, -1, resource().getValue(), initialUrlInfos());
	}

	private OPDSSyncNetworkLink(NetworkLibrary library, int id, String title, UrlInfoCollection<UrlInfoWithDate> infos) {
		super(library, id, title, null, null, infos);
	}

	public String getSummary() {
		final String account = SyncUtil.getAccountName(new QuietNetworkContext());
		return account != null ? account : resource().getResource("summary").getValue();
	}

	public Type getType() {
		return Type.Sync;
	}

	public boolean isLoggedIn(ZLNetworkContext context) {
		return SyncUtil.getAccountName(context) != null;
	}

	public void logout(ZLNetworkContext context) {
		SyncUtil.logout(context);
	}
}
