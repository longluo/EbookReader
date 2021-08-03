package com.longluo.zlibrary.core.fonts;

import com.longluo.util.ComparisonUtil;

import com.longluo.zlibrary.core.drm.FileEncryptionInfo;

public final class FileInfo {
	public final String Path;
	public final FileEncryptionInfo EncryptionInfo;

	public FileInfo(String path, FileEncryptionInfo encryptionInfo) {
		Path = path;
		EncryptionInfo = encryptionInfo;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FileInfo)) {
			return false;
		}
		final FileInfo oInfo = (FileInfo)other;
		return Path.equals(oInfo.Path) && ComparisonUtil.equal(EncryptionInfo, oInfo.EncryptionInfo);
	}

	@Override
	public int hashCode() {
		return Path.hashCode() + 23 * ComparisonUtil.hashCode(EncryptionInfo);
	}
}
