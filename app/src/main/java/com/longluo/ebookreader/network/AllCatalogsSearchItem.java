package com.longluo.ebookreader.network;

import java.util.LinkedList;

import com.longluo.zlibrary.core.network.*;
import com.longluo.zlibrary.core.util.MimeType;

import com.longluo.ebookreader.network.tree.NetworkItemsLoader;

public class AllCatalogsSearchItem extends SearchItem {
	private final NetworkLibrary myLibrary;

	public AllCatalogsSearchItem(NetworkLibrary library) {
		super(
			null,
			NetworkLibrary.resource().getResource("search").getResource("summaryAllCatalogs").getValue()
		);
		myLibrary = library;
	}

	@Override
	public void runSearch(ZLNetworkContext nc, NetworkItemsLoader loader, String pattern) throws ZLNetworkException {
		final LinkedList<ZLNetworkRequest> requestList = new LinkedList<ZLNetworkRequest>();
		final LinkedList<NetworkOperationData> dataList = new LinkedList<NetworkOperationData>();

		boolean containsCyrillicLetters = false;
		for (char c : pattern.toLowerCase().toCharArray()) {
			if ("абвгдеёжзийклмнопрстуфхцчшщъыьэюя".indexOf(c) >= 0) {
				containsCyrillicLetters = true;
				break;
			}
		}
		for (INetworkLink link : myLibrary.activeLinks()) {
			if (containsCyrillicLetters) {
				if ("ebooks.qumran.org".equals(link.getHostName())) {
					continue;
				}
			}
			final NetworkOperationData data = link.createOperationData(loader);
			final ZLNetworkRequest request = link.simpleSearchRequest(pattern, data);
			if (request != null) {
				dataList.add(data);
				requestList.add(request);
			}
		}

		while (!requestList.isEmpty()) {
			nc.perform(requestList);

			requestList.clear();

			if (loader.confirmInterruption()) {
				return;
			}
			for (NetworkOperationData data : dataList) {
				ZLNetworkRequest request = data.resume();
				if (request != null) {
					requestList.add(request);
				}
			}
		}
	}

	@Override
	public MimeType getMimeType() {
		return MimeType.APP_ATOM_XML;
	}

	@Override
	public String getUrl(String pattern) {
		return null;
	}
}
