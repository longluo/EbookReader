package com.longluo.ebookreader.network.opds;

import java.util.*;

import com.longluo.zlibrary.core.xml.ZLStringMap;
import com.longluo.zlibrary.core.money.Money;

import com.longluo.ebookreader.network.atom.ATOMLink;

class OPDSLink extends ATOMLink {
	public final LinkedList<Money> Prices = new LinkedList<Money>();
	public final LinkedList<String> Formats = new LinkedList<String>();

	protected OPDSLink(ZLStringMap attributes) {
		super(attributes);
	}

	private Money getPrice(String currency) {
		for (Money p : Prices) {
			if (currency.equals(p.Currency)) {
				return p;
			}
		}
		return null;
	}

	public Money selectBestPrice() {
		if (Prices.isEmpty()) {
			return null;
		} else if (Prices.size() == 1) {
			return Prices.get(0);
		}
		Money price;
		final Locale locale = Locale.getDefault();
		if (locale.getCountry().length() == 2) {
			final String bestCode = Currency.getInstance(locale).getCurrencyCode();
			if (bestCode != null) {
				price = getPrice(bestCode);
				if (price != null) {
					return price;
				}
			}
		}
		price = getPrice("USD");
		if (price != null) {
			return price;
		}
		price = getPrice("EUR");
		if (price != null) {
			return price;
		}
		return Prices.get(0);
	}
}
