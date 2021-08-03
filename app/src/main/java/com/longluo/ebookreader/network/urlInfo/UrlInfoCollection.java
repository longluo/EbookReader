package com.longluo.ebookreader.network.urlInfo;

import java.util.*;
import java.io.Serializable;

public class UrlInfoCollection<T extends UrlInfo> implements Serializable {
	private static final long serialVersionUID = -834589080548958222L;

	private final LinkedList<T> myInfos = new LinkedList<T>();

	public UrlInfoCollection(T ... elements) {
		for (T info : elements) {
			addInfo(info);
		}
	}

	public UrlInfoCollection(UrlInfoCollection<? extends T> other) {
		myInfos.addAll(other.myInfos);
	}

	public void upgrade(UrlInfoCollection<? extends T> other) {
		myInfos.removeAll(other.myInfos);
		myInfos.addAll(other.myInfos);
	}

	public void addInfo(T info) {
		if (info != null && info.InfoType != null) {
			myInfos.add(info);
		}
	}

	public T getInfo(UrlInfo.Type type) {
		for (T info : myInfos) {
			if (info.InfoType == type) {
				return info;
			}
		}
		return null;
	}

	public List<T> getAllInfos() {
		return Collections.unmodifiableList(myInfos);
	}

	public List<T> getAllInfos(UrlInfo.Type type) {
		List<T> list = null;
		for (T info : myInfos) {
			if (info.InfoType == type) {
				if (list == null) {
					list = new LinkedList<T>();
				}
				list.add(info);
			}
		}
		return list != null ? list : Collections.<T>emptyList();
	}

	public String getUrl(UrlInfo.Type type) {
		final T info = getInfo(type);
		return info != null ? info.Url : null;
	}

	public void clear() {
		myInfos.clear();
	}

	public void removeAllInfos(UrlInfo.Type type) {
		List<T> list = null;
		for (T info : myInfos) {
			if (info.InfoType == type) {
				if (list == null) {
					list = new LinkedList<T>();
				}
				list.add(info);
			}
		}

		if (list != null) {
			myInfos.removeAll(list);
		}
	}

	public boolean isEmpty() {
		return myInfos.isEmpty();
	}
}
