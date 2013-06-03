package com.dsa.pcapneo.domain.graph;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.dsa.pcapneo.domain.session.PcapSummary;


@NodeEntity
public class HttpSession extends IpSession {
	private static final Log log = LogFactory.getLog(HttpSession.class);
	private String url;
	
	@RelatedTo(type="CAME_FROM", direction=Direction.OUTGOING)
	private WebSite referer;
	
	@RelatedTo(type="CONNECTS_FROM", direction=Direction.OUTGOING)
	private Device device;
	
	@RelatedTo(type="CONNECTS_TO", direction=Direction.OUTGOING)
	private WebSite webSite;

	@RelatedTo(type="VIEWS", direction=Direction.OUTGOING)
	private WebResource resource;

	public HttpSession(PcapSummary pcap) {
		super(pcap);
		this.url = pcap.getHttpUrl();
		if (referer != null) {
			this.referer = new WebSite(pcap.getHttpReferer());
		}
		parseLocation(pcap.getHttpLocation());
		//TODO
		//Find device associated with this session
		//this.getIpSrc();
	}

	private void parseLocation(String location) {
		URI uri;
		try {
			uri = new URI(location);
			this.webSite = new WebSite(uri.getHost());
			this.resource = new WebResource(uri.getPath());
		} catch (URISyntaxException e) {
			log.error("Could not parse uri string: " + location, e);
		}
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public WebSite getReferer() {
		return referer;
	}

	public void setReferer(WebSite referer) {
		this.referer = referer;
	}

	public WebResource getResource() {
		return this.resource;
	}

	public void setResource(WebResource location) {
		this.resource = location;
	}
	
}
