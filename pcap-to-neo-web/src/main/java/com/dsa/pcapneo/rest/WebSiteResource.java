package com.dsa.pcapneo.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.WebSite;
import com.dsa.pcapneo.service.WebSiteRetrievalService;

@Component
@Path("/website")
@Produces(MediaType.APPLICATION_JSON)
public class WebSiteResource {
	private static final Log log = LogFactory.getLog(DeviceResource.class);
	
	@Autowired WebSiteRetrievalService siteService;

	@GET
	@Path("/{address}")
	public WebSite[] getWebSitesByAddress(@PathParam("address") String address) {
		WebSite[] sites = null;
		if (address != null && !address.isEmpty()) {
			sites = siteService.getWebSitesByAddress(address);
			log.info(String.format("Returning %s sites by address %s",sites.length, address));
		}
		return sites;
	}
	
}
