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
