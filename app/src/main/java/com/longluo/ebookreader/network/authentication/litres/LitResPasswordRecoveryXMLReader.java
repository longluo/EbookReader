package com.longluo.ebookreader.network.authentication.litres;

import com.longluo.zlibrary.core.xml.ZLStringMap;
import com.longluo.zlibrary.core.network.ZLNetworkException;

import com.longluo.ebookreader.network.NetworkException;

public class LitResPasswordRecoveryXMLReader extends LitResAuthenticationXMLReader {
	private static final String TAG_PASSWORD_RECOVERY_OK = "catalit-pass-recover-ok";
	private static final String TAG_PASSWORD_RECOVERY_FAILED = "catalit-pass-recover-failed";

	@Override
	public boolean startElementHandler(String tag, ZLStringMap attributes) {
		tag = tag.toLowerCase().intern();
		if (TAG_PASSWORD_RECOVERY_FAILED == tag) {
			final String error = attributes.getValue("error");
			if ("1".equals(error)) {
				setException(ZLNetworkException.forCode(NetworkException.ERROR_NO_USER_FOR_EMAIL));
			} else if ("2".equals(error)) {
				setException(ZLNetworkException.forCode(NetworkException.ERROR_EMAIL_NOT_SPECIFIED));
			} else {
				final String comment = attributes.getValue("coment");
				if (comment != null) {
					setException(new ZLNetworkException(comment));
				} else {
					setException(ZLNetworkException.forCode(NetworkException.ERROR_INTERNAL, error));
				}
			}
		} else if (TAG_PASSWORD_RECOVERY_OK == tag) {
			// NOP
		} else {
			setException(ZLNetworkException.forCode(ZLNetworkException.ERROR_SOMETHING_WRONG, LitResUtil.HOST_NAME));
		}
		return true;
	}
}
