package com.longluo.ebookreader.network.authentication.litres;

import com.longluo.zlibrary.core.xml.ZLStringMap;
import com.longluo.zlibrary.core.network.ZLNetworkAuthenticationException;
import com.longluo.zlibrary.core.network.ZLNetworkException;

public class LitResLoginXMLReader extends LitResAuthenticationXMLReader {
	private static final String TAG_AUTHORIZATION_OK = "catalit-authorization-ok";
	private static final String TAG_AUTHORIZATION_FAILED = "catalit-authorization-failed";

	public String FirstName;
	public String LastName;
	public String UserId;
	public String Sid;
	public boolean CanRebill;

	@Override
	public boolean startElementHandler(String tag, ZLStringMap attributes) {
		tag = tag.toLowerCase().intern();
		if (TAG_AUTHORIZATION_FAILED == tag) {
			setException(new ZLNetworkAuthenticationException());
		} else if (TAG_AUTHORIZATION_OK == tag) {
			FirstName = attributes.getValue("first-name");
			LastName = attributes.getValue("first-name");
			UserId = attributes.getValue("user-id");
			Sid = attributes.getValue("sid");
			String stringCanRebill = attributes.getValue("can-rebill");
			if (stringCanRebill == null) {
				stringCanRebill = attributes.getValue("can_rebill");
			}
			CanRebill = stringCanRebill != null && !"0".equals(stringCanRebill) && !"no".equalsIgnoreCase(stringCanRebill);
		} else {
			setException(ZLNetworkException.forCode(ZLNetworkException.ERROR_SOMETHING_WRONG, LitResUtil.HOST_NAME));
		}
		return true;
	}
}
