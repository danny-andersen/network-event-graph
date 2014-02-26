package com.dsa.pcapneo.rest;

import java.util.Date;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.session.PortUsage;
import com.dsa.pcapneo.service.PortRetrievalService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Component
@Path("/port/usage")
@Api(value="/port/usage", description="Find and retrieve Port usage")
@Produces(MediaType.APPLICATION_JSON)
public class PortResource extends Resource {
	private static final Log log = LogFactory.getLog(DeviceResource.class);

	@Autowired
	PortRetrievalService portService;

	@GET
	@Path("/session")
	@ApiOperation(value="Find Port usage by number of IP sessions for a port and time range", notes="Returns ports and number of sessions used in desc order")
	public PortUsage[] getPortBySessionUsage(
			@ApiParam(value="bottom of port range (0 if not set)", required = false)
			@DefaultValue("0") @QueryParam("minPort") 
			int minPort, 
			@ApiParam(value="top of port range (65536 if not set)", required = false)
			@DefaultValue("65535") @QueryParam("maxPort") 
			int maxPort,
			@ApiParam(value="start date", required = false)
			@QueryParam("startDate") 
			String startDate,
			@ApiParam(value="end date", required = false)
			@QueryParam("endDate") 
			String endDate
			) {
		long start = System.currentTimeMillis();
		PortUsage[] ports = null;
		if (minPort == 0 && startDate == null) {
			ports = portService.getMostUsedPorts();
		} else {
			ports = portService.getMostUsedPortsByRange(minPort, maxPort, parseDateString(startDate, 0), parseDateString(endDate, new Date().getTime()));
		}
		log.info(String.format(
				"Retrieved %d ports and session in port range: %d to %d in %d ms",
				ports.length, minPort, maxPort, System.currentTimeMillis() - start));
		return ports;
	}

	@GET
	@Path("/device")
	@ApiOperation(value="Find Port usage by number of devices it is connected to for a port and time range", notes="Returns ports and number of sessions used in desc order")
	public PortUsage[] getPortByDeviceUsage(
			@ApiParam(value="bottom of port range (0 if not set)", required = false)
			@DefaultValue("0") @QueryParam("minPort") 
			int minPort, 
			@ApiParam(value="top of port range (65536 if not set)", required = false)
			@DefaultValue("65536") @QueryParam("maxPort") 
			int maxPort,
			@ApiParam(value="start date", required = false)
			@QueryParam("startDate") 
			String startDate,
			@ApiParam(value="end date", required = false)
			@QueryParam("endDate") 
			String endDate
			) {
		long start = System.currentTimeMillis();
		PortUsage[] ports = null;
		if (minPort == 0 && startDate == null) {
			ports = portService.getMostConnectedPorts();
		} else {
			ports = portService.getMostConnectedPorts(minPort, maxPort, parseDateString(startDate, 0), parseDateString(endDate, new Date().getTime()));
		}
		log.info(String.format(
				"Retrieved %d ports and device usage in port range: %d to %d in %d ms",
				ports.length, minPort, maxPort, System.currentTimeMillis() - start));
		return ports;
	}
	
}
