package com.dsa.pcapneo.domain.session;

import com.dsa.pcapneo.domain.graph.Port;

public class PortUsage {
	private int sessionCount;
	private int deviceCount;
	private Port port;
	
	public int getSessionCount() {
		return sessionCount;
	}
	public void setSessionCount(int sessionCount) {
		this.sessionCount = sessionCount;
	}
	public Port getPort() {
		return port;
	}
	public void setPort(Port port) {
		this.port = port;
	}
	public int getDeviceCount() {
		return deviceCount;
	}
	public void setDeviceCount(int deviceCount) {
		this.deviceCount = deviceCount;
	}
	
}
