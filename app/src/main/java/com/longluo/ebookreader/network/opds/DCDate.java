package com.longluo.ebookreader.network.opds;

import com.longluo.zlibrary.core.xml.ZLStringMap;

import com.longluo.ebookreader.network.atom.*;

class DCDate extends ATOMDateConstruct {
	protected DCDate(ZLStringMap source) {
		super(source);
	}

	/*
	public DCDate(int year) {
		super(year);
	}

	public DCDate(int year, int month, int day) {
		super(year, month, day);
	}

	public DCDate(int year, int month, int day, int hour, int minutes, int seconds) {
		super(year, month, day, hour, minutes, seconds);
	}

	public DCDate(int year, int month, int day, int hour, int minutes, int seconds, float sfract) {
		super(year, month, day, hour, minutes, seconds, sfract);
	}

	public DCDate(int year, int month, int day, int hour, int minutes, int seconds, float sfract, int tzhour, int tzminutes) {
		super(year, month, day, hour, minutes, seconds, sfract, tzhour, tzminutes);
	}
	*/
}
