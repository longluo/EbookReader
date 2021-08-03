package com.longluo.ebookreader.network.authentication.litres;

import com.longluo.zlibrary.core.xml.ZLStringMap;
import com.longluo.zlibrary.core.network.ZLNetworkException;

import com.longluo.ebookreader.network.NetworkException;

public class LitResRegisterUserXMLReader extends LitResAuthenticationXMLReader {
	private static final String TAG_AUTHORIZATION_OK = "catalit-authorization-ok";
	private static final String TAG_REGISTRATION_FAILED = "catalit-registration-failed";

	public String Sid;

	public static final class AlreadyInUseException extends ZLNetworkException {
		public AlreadyInUseException(String code) {
			super(errorMessage(code));
		}
	}

	@Override
	public boolean startElementHandler(String tag, ZLStringMap attributes) {
		tag = tag.toLowerCase().intern();
		if (TAG_REGISTRATION_FAILED == tag) {
			final String error = attributes.getValue("error");
			if ("1".equals(error)) {
				setException(new AlreadyInUseException("usernameAlreadyInUse"));
			} else if ("2".equals(error)) {
				setException(ZLNetworkException.forCode("usernameNotSpecified"));
			} else if ("3".equals(error)) {
				setException(ZLNetworkException.forCode("passwordNotSpecified"));
			} else if ("4".equals(error)) {
				setException(ZLNetworkException.forCode("invalidEMail"));
			} else if ("5".equals(error)) {
				setException(ZLNetworkException.forCode("tooManyRegistrations"));
			} else if ("6".equals(error)) {
				setException(new AlreadyInUseException("emailAlreadyInUse"));
			} else {
				final String comment = attributes.getValue("coment");
				if (comment != null) {
					setException(new ZLNetworkException(comment));
				} else {
					setException(ZLNetworkException.forCode(NetworkException.ERROR_INTERNAL));
				}
			}
		} else if (TAG_AUTHORIZATION_OK == tag) {
			Sid = attributes.getValue("sid");
			if (Sid == null) {
				setException(ZLNetworkException.forCode("somethingWrongMessage", LitResUtil.HOST_NAME));
			}
		} else {
			setException(ZLNetworkException.forCode("somethingWrongMessage", LitResUtil.HOST_NAME));
		}
		return true;
	}
}
