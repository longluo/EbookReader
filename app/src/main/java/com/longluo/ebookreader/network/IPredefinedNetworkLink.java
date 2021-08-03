package com.longluo.ebookreader.network;

public interface IPredefinedNetworkLink extends INetworkLink {
	String getPredefinedId();
	boolean servesHost(String hostname);
}
