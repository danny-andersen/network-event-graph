package com.dsa.pcapneo.rest;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.service.SessionRetrievalService;

@Component
@Path("/session")
@Produces(MediaType.APPLICATION_JSON)
public class SessionResource {
	private static final Log log = LogFactory.getLog(SessionResource.class);
	
	@Autowired SessionRetrievalService sessionService;

}
