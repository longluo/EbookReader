package com.longluo.ebookreader.fbreader.options;

import com.longluo.zlibrary.core.options.ZLBooleanOption;
import com.longluo.zlibrary.core.options.ZLIntegerRangeOption;

public class EInkOptions {
	public final ZLBooleanOption EnableFastRefresh =
		new ZLBooleanOption("EInk", "EnableFastRefresh", true);
	public final ZLIntegerRangeOption UpdateInterval =
		new ZLIntegerRangeOption("EInk", "UpdateInterval", 0, 20, 10);
}
