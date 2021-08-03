package com.longluo.ebookreader.network.opds;

import java.util.List;
import java.util.LinkedList;

import com.longluo.zlibrary.core.xml.ZLStringMap;

import com.longluo.ebookreader.network.atom.*;

class OPDSEntry extends ATOMEntry {
	public String DCLanguage;
	public String DCPublisher;
	public DCDate DCIssued;
	public final List<String> DCIdentifiers = new LinkedList<String>();

	public String SeriesTitle;
	public float SeriesIndex;

	protected OPDSEntry(ZLStringMap attributes) {
		super(attributes);
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder("[");
		buf.append(super.toString());
		buf.append(",DCLanguage=").append(DCLanguage);
		buf.append(",DCPublisher=").append(DCPublisher);
		buf.append(",DCIssued=").append(DCIssued);
		buf.append(",SeriesTitle=").append(SeriesTitle);
		buf.append(",SeriesIndex=").append(SeriesIndex);
		buf.append("]");
		return buf.toString();
	}
}
