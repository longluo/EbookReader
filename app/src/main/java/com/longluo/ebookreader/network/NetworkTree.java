package com.longluo.ebookreader.network;

import java.util.*;

import com.longluo.zlibrary.core.image.ZLImage;
import com.longluo.zlibrary.core.util.MimeType;

import com.longluo.ebookreader.network.urlInfo.UrlInfo;
import com.longluo.ebookreader.tree.FBTree;

public abstract class NetworkTree extends FBTree {
	public final NetworkLibrary Library;

	protected NetworkTree(NetworkLibrary library) {
		super();
		Library = library;
	}

	protected NetworkTree(NetworkTree parent) {
		super(parent);
		Library = parent.Library;
	}

	protected NetworkTree(NetworkTree parent, int position) {
		super(parent, position);
		Library = parent.Library;
	}

	@Override
	public String getSummary() {
		StringBuilder builder = new StringBuilder();
		int count = 0;
		for (FBTree subtree : subtrees()) {
			if (count++ > 0) {
				builder.append(",  ");
			}
			builder.append(subtree.getName());
			if (count == 5) {
				break;
			}
		}
		return builder.toString();
	}

	public INetworkLink getLink() {
		final NetworkTree parent = (NetworkTree)Parent;
		return parent != null ? parent.getLink() : null;
	}

	public static ZLImage createCoverForItem(NetworkLibrary library, NetworkItem item, boolean thumbnail) {
		String coverUrl = item.getUrl(thumbnail ? UrlInfo.Type.Thumbnail : UrlInfo.Type.Image);
		if (coverUrl == null) {
			coverUrl = item.getUrl(thumbnail ? UrlInfo.Type.Image : UrlInfo.Type.Thumbnail);
		}
		if (coverUrl == null) {
			return null;
		}
		return createCoverFromUrl(library, coverUrl, null);
	}

	private static final String DATA_PREFIX = "data:";

	public static ZLImage createCoverFromUrl(NetworkLibrary library, String url, MimeType mimeType) {
		if (url == null) {
			return null;
		}
		if (mimeType == null) {
			mimeType = MimeType.IMAGE_AUTO;
		}
		if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("ftp://")) {
			return library.getImageByUrl(url, mimeType);
		} else if (url.startsWith(DATA_PREFIX)) {
			int commaIndex = url.indexOf(',');
			if (commaIndex == -1) {
				return null;
			}
			if (mimeType == MimeType.IMAGE_AUTO) {
				int index = url.indexOf(';');
				if (index == -1 || index > commaIndex) {
					index = commaIndex;
				}
	 			// string starts with "data:image/"
				if (url.startsWith(MimeType.IMAGE_PREFIX, DATA_PREFIX.length())) {
					mimeType = MimeType.get(url.substring(DATA_PREFIX.length(), index));
				}
			}
			int key = url.indexOf("base64");
			if (key != -1 && key < commaIndex) {
				Base64EncodedImage img = new Base64EncodedImage(
					library, url.substring(commaIndex + 1), mimeType
				);
				return img;
			}
		}
		return null;
	}

	public void removeTrees(Set<NetworkTree> trees) {
		if (trees.isEmpty() || subtrees().isEmpty()) {
			return;
		}
		final LinkedList<FBTree> toRemove = new LinkedList<FBTree>();
		for (FBTree t : subtrees()) {
			if (trees.contains(t)) {
				toRemove.add(t);
				trees.remove(t);
			}
		}
		for (FBTree tree : toRemove) {
			tree.removeSelf();
		}
		if (trees.isEmpty()) {
			return;
		}

		final LinkedList<FBTree> toProcess = new LinkedList<FBTree>(subtrees());
		while (!toProcess.isEmpty()) {
			((NetworkTree)toProcess.remove(toProcess.size() - 1)).removeTrees(trees);
		}
	}
}
