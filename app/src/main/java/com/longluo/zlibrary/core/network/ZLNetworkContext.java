package com.longluo.zlibrary.core.network;

import java.io.*;

import java.util.HashSet;
import java.util.List;

import org.apache.http.cookie.Cookie;

import com.longluo.zlibrary.core.options.ZLStringOption;

public abstract class ZLNetworkContext implements ZLNetworkManager.BearerAuthenticator {
	private final ZLNetworkManager myManager = ZLNetworkManager.Instance();

	protected ZLNetworkContext() {
	}

	public ZLNetworkManager.CookieStore cookieStore() {
		return myManager.CookieStore;
	}

	public void removeCookiesForDomain(String domain) {
		myManager.CookieStore.clearDomain(domain);
	}

	public void reloadCookie() {
		myManager.CookieStore.reset();
	}

	public String getCookieValue(String domain, String name) {
		for (Cookie c : cookieStore().getCookies()) {
			if (domain.equals(c.getDomain()) && name.equals(c.getName())) {
				return c.getValue();
			}
		}
		return null;
	}

	@Override
	public String getAccountName(String host, String realm) {
		final String accountName = getAccountOption(host, realm).getValue();
		return "".equals(accountName) ? null : accountName;
	}

	@Override
	public void setAccountName(String host, String realm, String accountName) {
		getAccountOption(host, realm).setValue(accountName != null ? accountName : "");
	}

	protected void perform(ZLNetworkRequest request, int socketTimeout, int connectionTimeout) throws ZLNetworkException {
		myManager.perform(request, this, socketTimeout, connectionTimeout);
	}

	public final void perform(ZLNetworkRequest request) throws ZLNetworkException {
		perform(request, 30000, 15000);
	}

	public final boolean performQuietly(ZLNetworkRequest request) {
		try {
			perform(request);
			return true;
		} catch (ZLNetworkException e) {
			return false;
		}
	}

	public final void perform(List<? extends ZLNetworkRequest> requests) throws ZLNetworkException {
		if (requests.size() == 0) {
			return;
		}
		if (requests.size() == 1) {
			perform(requests.get(0));
			return;
		}
		HashSet<String> errors = new HashSet<String>();
		// TODO: implement concurrent execution !!!
		for (ZLNetworkRequest r : requests) {
			try {
				perform(r);
			} catch (ZLNetworkException e) {
				e.printStackTrace();
				errors.add(e.getMessage());
			}
		}
		if (errors.size() > 0) {
			StringBuilder message = new StringBuilder();
			for (String e : errors) {
				if (message.length() != 0) {
					message.append(", ");
				}
				message.append(e);
			}
			throw new ZLNetworkException(message.toString());
		}
	}

	public final void downloadToFile(String url, final File outFile) throws ZLNetworkException {
		downloadToFile(url, outFile, 8192);
	}

	private final void downloadToFile(String url, final File outFile, final int bufferSize) throws ZLNetworkException {
		perform(new ZLNetworkRequest.Get(url) {
			public void handleStream(InputStream inputStream, int length) throws IOException, ZLNetworkException {
				OutputStream outStream = new FileOutputStream(outFile);
				try {
					final byte[] buffer = new byte[bufferSize];
					while (true) {
						final int size = inputStream.read(buffer);
						if (size <= 0) {
							break;
						}
						outStream.write(buffer, 0, size);
					}
				} finally {
					outStream.close();
				}
			}
		}, 0, 0);
	}

	private ZLStringOption getAccountOption(String host, String realm) {
		return new ZLStringOption("auth", host + ":" + realm, "");
	}
}
