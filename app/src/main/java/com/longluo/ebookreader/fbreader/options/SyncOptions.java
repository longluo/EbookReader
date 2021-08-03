package com.longluo.ebookreader.fbreader.options;

import com.longluo.zlibrary.core.options.*;

public class SyncOptions {
	public static final String DOMAIN = "books.fbreader.org";
	public static final String BASE_URL = "https://" + DOMAIN + "/";
	public static final String OPDS_URL = "https://" + DOMAIN + "/opds";
	public static final String REALM = "FBReader book network";

	public final ZLBooleanOption Enabled =
		new ZLBooleanOption("Sync", "Enabled", false);

	public static enum Condition {
		never, viaWifi, always
	}
	public final ZLEnumOption<Condition> UploadAllBooks =
		new ZLEnumOption<Condition>("Sync", "UploadAllBooks", Condition.viaWifi);
	public final ZLEnumOption<Condition> Positions =
		new ZLEnumOption<Condition>("Sync", "Positions", Condition.always);
	public final ZLBooleanOption ChangeCurrentBook =
		new ZLBooleanOption("Sync", "ChangeCurrentBook", true);
	public final ZLEnumOption<Condition> Bookmarks =
		new ZLEnumOption<Condition>("Sync", "Bookmarks", Condition.always);
	public final ZLEnumOption<Condition> CustomShelves =
		new ZLEnumOption<Condition>("Sync", "CustomShelves", Condition.always);
	public final ZLEnumOption<Condition> Metainfo =
		new ZLEnumOption<Condition>("Sync", "Metainfo", Condition.always);
}
