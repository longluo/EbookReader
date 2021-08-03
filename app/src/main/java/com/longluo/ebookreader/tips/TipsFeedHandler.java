package com.longluo.ebookreader.tips;

import java.util.*;

import com.longluo.ebookreader.network.atom.*;

class TipsFeedHandler extends AbstractATOMFeedHandler {
	final List<Tip> Tips = new LinkedList<Tip>();

	@Override
	public boolean processFeedEntry(ATOMEntry entry) {
		Tips.add(new Tip(entry.Title, entry.Content));
		return false;
	}
}
