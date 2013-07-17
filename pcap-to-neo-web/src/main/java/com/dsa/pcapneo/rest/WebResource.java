package com.dsa.pcapneo.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.WebSite;
import com.dsa.pcapneo.service.WebSiteRetrievalService;

@Component
@Path("/websitevisit")
@Produces(MediaType.APPLICATION_JSON)
public class WebResource {
	private static final Log log = LogFactory.getLog(DeviceResource.class);
	
	@Autowired WebSiteRetrievalService siteService;

	@GET
	public WebSite[] getWebSitesVisited(@QueryParam("hostname") String hostname, @QueryParam("ipaddr") String ipAddr) {
		WebSite[] sites = null;
		if (hostname != null && !hostname.isEmpty()) {
			sites = siteService.getWebSitesVisitedByHostname(hostname);
			log.info(String.format("Returning %s sites visited by hostname %s",sites.length, hostname));
		} else if (ipAddr != null && !ipAddr.isEmpty()) {
			sites = siteService.getWebSitesVisitedByIpAddr(ipAddr);
			log.info(String.format("Returning %s sites visited by ipAddr %s",sites.length, ipAddr));
		}
		return sites;
	}
	
}
