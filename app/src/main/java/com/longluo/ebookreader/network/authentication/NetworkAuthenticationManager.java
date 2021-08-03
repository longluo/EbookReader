package com.longluo.ebookreader.network.authentication;

import java.util.*;

import com.longluo.zlibrary.core.options.ZLStringOption;
import com.longluo.zlibrary.core.network.ZLNetworkException;
import com.longluo.zlibrary.core.money.Money;

import com.longluo.ebookreader.network.*;
import com.longluo.ebookreader.network.opds.OPDSNetworkLink;
import com.longluo.ebookreader.network.authentication.litres.LitResAuthenticationManager;
import com.longluo.ebookreader.network.urlInfo.*;

public abstract class NetworkAuthenticationManager {
	private static final HashMap<String,NetworkAuthenticationManager> ourManagers = new HashMap<String,NetworkAuthenticationManager>();

	public static NetworkAuthenticationManager createManager(NetworkLibrary library, INetworkLink link, Class<? extends NetworkAuthenticationManager> managerClass) {
		NetworkAuthenticationManager mgr = ourManagers.get(link.getStringId());
		if (mgr == null) {
			if (managerClass == LitResAuthenticationManager.class) {
				mgr = new LitResAuthenticationManager(library, (OPDSNetworkLink)link);
			}
			if (mgr != null) {
				ourManagers.put(link.getStringId(), mgr);
			}
		}
		return mgr;
	}


	public final INetworkLink Link;
	protected final ZLStringOption UserNameOption;

	protected NetworkAuthenticationManager(INetworkLink link) {
		Link = link;
		UserNameOption = new ZLStringOption(link.getStringId(), "userName", "");
	}

	public String getUserName() {
		return UserNameOption.getValue();
	}

	public String getVisibleUserName() {
		final String username = getUserName();
		return username.startsWith("fbreader-auto-") ? "auto" : username;
	}

	/*
	 * Common manager methods
	 */
	public abstract boolean isAuthorised(boolean useNetwork /* = true */) throws ZLNetworkException;
	public abstract void authorise(String username, String password) throws ZLNetworkException;
	public abstract void logOut();
	public abstract BookUrlInfo downloadReference(NetworkBookItem book);
	public abstract void refreshAccountInformation() throws ZLNetworkException;

	public final boolean mayBeAuthorised(boolean useNetwork) {
		try {
			return isAuthorised(useNetwork);
		} catch (ZLNetworkException e) {
		}
		return true;
	}

	public boolean needsInitialization() {
		return false;
	}

	public void initialize() throws ZLNetworkException {
		throw ZLNetworkException.forCode(NetworkException.ERROR_UNSUPPORTED_OPERATION);
	}

	// returns true if link must be purchased before downloading
	public boolean needPurchase(NetworkBookItem book) {
		return true;
	}

	public void purchaseBook(NetworkBookItem book) throws ZLNetworkException {
		throw ZLNetworkException.forCode(NetworkException.ERROR_UNSUPPORTED_OPERATION);
	}

	public List<NetworkBookItem> purchasedBooks() {
		return Collections.emptyList();
	}

	public Money currentAccount() {
		return null;
	}

	/*
	 * topup account
	 */

	public String topupLink(Money sum) {
		return null;
	}
	public Map<String,String> getTopupData() {
		return Collections.emptyMap();
	}

	/*
	 * Password Recovery
	 */
	public boolean passwordRecoverySupported() {
		return false;
	}

	public void recoverPassword(String email) throws ZLNetworkException {
		throw ZLNetworkException.forCode(NetworkException.ERROR_UNSUPPORTED_OPERATION);
	}
}
