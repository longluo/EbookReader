package com.longluo.ebookreader.network;

import java.util.*;

import com.longluo.zlibrary.core.network.ZLNetworkRequest;

import com.longluo.ebookreader.network.authentication.NetworkAuthenticationManager;
import com.longluo.ebookreader.network.urlInfo.UrlInfo;
import com.longluo.ebookreader.network.urlInfo.UrlInfoWithDate;
import com.longluo.ebookreader.network.tree.NetworkItemsLoader;

public interface INetworkLink extends Comparable<INetworkLink> {
	public enum Type {
		Predefined(0),
		Custom(1),
		Local(2),
		Sync(3);

		public final int Index;

		Type(int index) {
			Index = index;
		}

		public static Type byIndex(int index) {
			for (Type t : Type.values()) {
				if (t.Index == index) {
					return t;
				}
			}
			return Custom;
		}
	};

	public enum AccountStatus {
		NotSupported,
		NoUserName,
		SignedIn,
		SignedOut,
		NotChecked
	};

	public static final int INVALID_ID = -1;

	int getId();
	void setId(int id);

	String getStringId();
	String getShortName();
	String getHostName();
	String getTitle();
	String getSummary();

	String getUrl(UrlInfo.Type type);
	UrlInfoWithDate getUrlInfo(UrlInfo.Type type);
	Set<UrlInfo.Type> getUrlKeys();

	/**
	 * @param force if local status is not checked then
     *    if force is set to false, NotChecked will be returned
     *    if force is set to true, network check will be performed;
	 *       that will take some time and can return NotChecked (if network is not available)
     */
	//AccountStatus getAccountStatus(boolean force);

	Type getType();

	/**
	 * @return 2-letters language code or special token "multi"
	 */
	String getLanguage();

	/**
	 * @param listener Network operation listener
	 * @return instance, which represents the state of the network operation.
	 */
	NetworkOperationData createOperationData(NetworkItemsLoader loader);

	BasketItem getBasketItem();

	ZLNetworkRequest simpleSearchRequest(String pattern, NetworkOperationData data);
	ZLNetworkRequest resume(NetworkOperationData data);

	NetworkCatalogItem libraryItem();
	NetworkAuthenticationManager authenticationManager();

	String rewriteUrl(String url, boolean isUrlExternal);
}
