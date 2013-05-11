package com.dsa.pcapneo.domain;

public class HttpSession extends IpSession {
	private String url;
	private String referer;
	private String location;

	public HttpSession(PcapSummary pcap) {
		super(pcap);
		this.url = pcap.getHttpUrl();
		this.referer = pcap.getHttpReferer();
		this.location = pcap.getHttpLocation();
	}

	@Override
	public void insertSelfIntoGraph() {
		super.insertSelfIntoGraph();
		//Add Http relationship(s)
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
}
