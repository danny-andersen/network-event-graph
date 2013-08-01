package com.dsa.pcapneo.domain.graph;


public class SessionSummary {
	protected long earliest;
	protected long latest;
	protected long srcDeviceId;
	protected long destDeviceId;
	protected String srcHostname;
	protected String destHostname;
	protected long numSessions;
	protected String srcIpAddr;
	protected String destIpAddr;
	protected String webAddress;

	public String getDestIpAddr() {
		return destIpAddr;
	}
	public void setDestIpAddr(String destIpAddr) {
		this.destIpAddr = destIpAddr;
	}
	public String getWebAddress() {
		return webAddress;
	}
	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}
	public long getEarliest() {
		return earliest;
	}
	public void setEarliest(long earliest) {
		this.earliest = earliest;
	}
	public long getLatest() {
		return latest;
	}
	public void setLatest(long latest) {
		this.latest = latest;
	}
	public long getNumSessions() {
		return numSessions;
	}
	public void setNumSessions(long numSessions) {
		this.numSessions = numSessions;
	}
	public String getSrcIpAddr() {
		return srcIpAddr;
	}
	public void setSrcIpAddr(String srcIpAddr) {
		this.srcIpAddr = srcIpAddr;
	}
	public long getSrcDeviceId() {
		return srcDeviceId;
	}
	public void setSrcDeviceId(long srcDeviceId) {
		this.srcDeviceId = srcDeviceId;
	}
	public long getDestDeviceId() {
		return destDeviceId;
	}
	public void setDestDeviceId(long destDeviceId) {
		this.destDeviceId = destDeviceId;
	}
	public String getSrcHostname() {
		return srcHostname;
	}
	public void setSrcHostname(String srcHostname) {
		this.srcHostname = srcHostname;
	}
	public String getDestHostname() {
		return destHostname;
	}
	public void setDestHostname(String destHostname) {
		this.destHostname = destHostname;
	}
	
}
