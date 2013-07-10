package com.dsa.pcapneo.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.WebSite;

@Component
@Path("/website")
@Produces(MediaType.APPLICATION_JSON)
public class WebResource {
	@GET
	public WebSite[] getWebSitesByUrl(@QueryParam("url") String url) {
		return null;
	}
}
