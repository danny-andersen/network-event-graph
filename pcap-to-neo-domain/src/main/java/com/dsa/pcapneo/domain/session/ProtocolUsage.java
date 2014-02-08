package com.dsa.pcapneo.domain.session;

import com.dsa.pcapneo.domain.graph.Protocol;

public class ProtocolUsage {
	private int sessionCount;
	private int deviceCount;
	private Protocol protocol;
	
	public int getSessionCount() {
		return sessionCount;
	}
	public void setSessionCount(int sessionCount) {
		this.sessionCount = sessionCount;
	}
	public int getDeviceCount() {
		return deviceCount;
	}
	public void setDeviceCount(int deviceCount) {
		this.deviceCount = deviceCount;
	}
	public Protocol getProtocol() {
		return protocol;
	}
	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}
	
}
