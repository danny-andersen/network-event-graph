package com.dsa.pcapneo.domain.graph;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.dsa.pcapneo.domain.session.PcapSummary;
import com.dsa.pcapneo.domain.session.SessionArtefactFactory;


@NodeEntity
public class HttpSession extends IpSession {
	private static final Log log = LogFactory.getLog(HttpSession.class);
	
	@RelatedTo(type="CAME_FROM", direction=Direction.OUTGOING)
	private WebSite referer;
	
	@RelatedTo(type="CONNECTS_FROM", direction=Direction.OUTGOING)
	private Device device;
	
	@RelatedTo(type="CONNECTS_TO", direction=Direction.OUTGOING)
	private WebSite webSite;

	@RelatedTo(type="VIEWS", direction=Direction.OUTGOING)
	private WebPath resource;

	public HttpSession() {
		super();
	};
	
	public HttpSession(SessionArtefactFactory factory) {
		super(factory);
	};

	public HttpSession(SessionArtefactFactory factory, PcapSummary pcap) {
		super(factory, pcap);
		if (pcap.getHttpReferer() != null) {
			this.setReferer(factory.getWebSite(pcap.getHttpReferer()));
		}
		//Use URL if set otherwise use location
		if (pcap.getHttpUrl() != null) {
			parseUri(pcap.getHttpUrl());
		} else if (pcap.getHttpLocation() != null) {
			parseUri(pcap.getHttpLocation());
		}
		this.setDevice(factory.getDeviceFromIpAddr(this.getIpSrc()));
	}

	public void setUri(String url) {
		parseUri(url);
	}
	
	private void parseUri(String location) {
		if (location == null) {
			return;
		}
		if (this.factory == null) {
			log.warn("Cannot parse uri as SessionArtfactFactory is not set");
		}
		URI uri = null;
		try {
			uri = new URI(location);
			this.webSite = factory.getWebSite(uri.getHost());
			this.resource = factory.getWebPath(uri.getPath());
		} catch (URISyntaxException e) {
			log.error("Could not parse uri string: " + location, e);
		}
	}
	
	public void setUrl(String url) {
		parseUri(url);
	}
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public WebSite getWebSite() {
		return webSite;
	}

	public void setWebSite(WebSite webSite) {
		this.webSite = webSite;
	}

	public WebSite getReferer() {
		return referer;
	}

	public void setReferer(WebSite referer) {
		this.referer = referer;
	}

	public WebPath getResource() {
		return this.resource;
	}

	public void setResource(WebPath location) {
		this.resource = location;
	}
	
}
