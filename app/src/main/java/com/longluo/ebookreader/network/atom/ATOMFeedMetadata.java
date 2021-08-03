package com.longluo.ebookreader.network.atom;

import java.util.*;

import com.longluo.zlibrary.core.xml.ZLStringMap;

public class ATOMFeedMetadata extends ATOMCommonAttributes {
	public ATOMId Id;

	public LinkedList<ATOMAuthor> Authors = new LinkedList<ATOMAuthor>();
	public LinkedList<ATOMCategory> Categories = new LinkedList<ATOMCategory>();
	//public LinkedList<ATOMContributor> Contributors = new LinkedList<ATOMContributor>();
	//public ATOMGenerator Generator;
	public ATOMIcon Icon;
	public LinkedList<ATOMLink> Links = new LinkedList<ATOMLink>();
	//public ATOMLogo Logo;
	//public String Rights;   // TODO: implement ATOMTextConstruct
	public CharSequence Subtitle; // TODO: implement ATOMTextConstruct
	public CharSequence Title;    // TODO: implement ATOMTextConstruct
	public ATOMUpdated Updated;

	protected ATOMFeedMetadata(ZLStringMap source) {
		super(source);
	}
}
