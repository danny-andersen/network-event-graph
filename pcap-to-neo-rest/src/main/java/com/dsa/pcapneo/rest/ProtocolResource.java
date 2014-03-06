package com.dsa.pcapneo.rest;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.session.ProtocolUsage;
import com.dsa.pcapneo.service.ProtocolRetrievalService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Component
@Path("/protocol")
@Api(value="/protocol", description="Find and retrieve Protocol usage")
@Produces(MediaType.APPLICATION_JSON)

public class ProtocolResource extends Resource {
	private static final Log log = LogFactory.getLog(DeviceResource.class);

	@Autowired
	ProtocolRetrievalService protoService;
	
	@GET
	@Path("/usage")
	@ApiOperation(value="Find Protocol usage for a protocol name by number of IP sessions for a time range", notes="Returns ports and number of sessions used in desc order")
	public ProtocolUsage[] findProtocolUsage(
			@ApiParam(value="protocol", required = false)
			@QueryParam("protocol") String protocol,
			@ApiParam(value="start date", required = false)
			@QueryParam("startDate") 
			String startDate,
			@ApiParam(value="end date", required = false)
			@QueryParam("endDate") 
			String endDate
			) {
		long start = System.currentTimeMillis();
		ProtocolUsage[] pu = null;
		if (protocol == null && startDate == null) {
			pu = protoService.getAllProtocolUsage();
		} else {
			pu = protoService.getProtocolUsage(protocol, parseDateString(startDate, 0), parseDateString(endDate, new Date().getTime()));
		}
		log.info(String.format(
				"Retrieved %d protocols and usage for protocol: %s in %d ms",
				pu.length, protocol == null ? "*" : protocol, System.currentTimeMillis() - start));
		return pu;
	}

	@GET
	@Path("/usage/port")
	@ApiOperation(value="Find Protocol usage for a port by number of IP sessions for a port and time range", notes="Returns ports and number of sessions used in desc order")
	public ProtocolUsage[] findProtocolUsageByPort(
			@ApiParam(value="port number", required = true)
			@QueryParam("port") int port,
			@ApiParam(value="start date", required = false)
			@QueryParam("startDate") 
			String startDate,
			@ApiParam(value="end date", required = false)
			@QueryParam("endDate")
			String endDate
			) {
		long start = System.currentTimeMillis();
		ProtocolUsage[] pu = null;
		pu = protoService.getProtocolsUsedOnPort(port, parseDateString(startDate, 0), parseDateString(endDate, new Date().getTime()));
		log.info(String.format(
				"Retrieved %d protocols and usage for port: %s in %d ms",
				pu.length, port, System.currentTimeMillis() - start));
		return pu;
	}

	@GET
	@Path("/usage/device")
	@ApiOperation(value="Find Protocol usage for a device by number of IP sessions for a port and time range", notes="Returns ports and number of sessions used in desc order")
	public ProtocolUsage[] findProtocolUsageByDevice(
			@ApiParam(value="device id", required = true)
			@QueryParam("deviceId") long deviceId,
			@ApiParam(value="start date", required = false)
			@QueryParam("startDate") 
			String startDate,
			@ApiParam(value="end date", required = false)
			@QueryParam("endDate") 
			String endDate
			) {
		long start = System.currentTimeMillis();
		ProtocolUsage[] pu = null;
		pu = protoService.getProtocolsUsedByDevice(deviceId, parseDateString(startDate, 0), parseDateString(endDate, new Date().getTime()));
		log.info(String.format(
				"Retrieved %d protocols and usage for device: %s in %d ms",
				pu.length, deviceId, System.currentTimeMillis() - start));
		return pu;
	}
}
