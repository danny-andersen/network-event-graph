package com.dsa.pcapneo.rest;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.IpSession;
import com.dsa.pcapneo.domain.graph.SessionSummary;
import com.dsa.pcapneo.graph.repositories.SessionRepository.SessionQueryType;
import com.dsa.pcapneo.service.SessionRetrievalService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Component
@Path("/session/ip")
@Api(value="/session/ip", description="Find and retrieve IP Sessions")
@Produces(MediaType.APPLICATION_JSON)
public class IpSessionResource extends Resource {
	private static final Log log = LogFactory.getLog(IpSessionResource.class);
	@Autowired
	SessionRetrievalService sessionService;

	@GET
	@Path("/summary/ipaddr/{ipaddr}")
	@ApiOperation(value="Retrieve Ip session summaries for IP address", notes="Specify an IP address to view all IP sessions, filtered by date")
	public SessionSummary[] getIpSessionSummariesByIpAddrBoth(
			@ApiParam(value="ip address", required = true)
			@PathParam("ipaddr") String ipAddr,
			@ApiParam(value="start date", required = false)
			@QueryParam("startdate") String startDate,
			@ApiParam(value="end date", required = false)
			@QueryParam("enddate") String endDate) {
		return getIpSessionSummariesByIpAddr(ipAddr, startDate, endDate,
				SessionQueryType.BOTH);
	}

	@GET
	@Path("/summary/ipaddr/src/{ipaddr}")
	@ApiOperation(value="Retrieve Ip session summaries for source IP address", notes="Specify a source IP address to view all IP sessions, filtered by date")
	public SessionSummary[] getIpSessionSummariesByIpAddrSrc(
			@ApiParam(value="source ip address", required = true)
			@PathParam("ipaddr") String ipAddr,
			@ApiParam(value="start date", required = false)
			@QueryParam("startdate") String startDate,
			@ApiParam(value="end date", required = false)
			@QueryParam("enddate") String endDate) {
		return getIpSessionSummariesByIpAddr(ipAddr, startDate, endDate,
				SessionQueryType.SRC);
	}

	@GET
	@Path("/summary/ipaddr/dest/{ipaddr}")
	@ApiOperation(value="Retrieve Ip session summaries for destination IP address", notes="Specify a destination IP address to view all IP sessions, filtered by date")
	public SessionSummary[] getIpSessionSummariesByIpAddrDest(
			@ApiParam(value="destination ip address", required = true)
			@PathParam("ipaddr") String ipAddr,
			@ApiParam(value="start date", required = false)
			@QueryParam("startdate") String startDate,
			@ApiParam(value="end date", required = false)
			@QueryParam("enddate") String endDate) {
		return getIpSessionSummariesByIpAddr(ipAddr, startDate, endDate,
				SessionQueryType.DEST);
	}

	@GET
	@Path("/summary/device/{deviceid}")
	@ApiOperation(value="Retrieve Ip session summaries for a device", notes="Specify a device Id to view all IP sessions, filtered by date")
	public SessionSummary[] getIpSessionSummariesByDevice(
			@ApiParam(value="deviceId", required = true)
			@PathParam("deviceid") long id,
			@ApiParam(value="start date", required = false)
			@QueryParam("startdate") String startDate,
			@ApiParam(value="end date", required = false)
			@QueryParam("enddate") String endDate) {
		return getIpSessionSummariesByDevice(id, startDate, endDate,
				SessionQueryType.BOTH);
	}

	@GET
	@Path("/summary/device/src/{deviceid}")
	@ApiOperation(value="Retrieve Ip session summaries for a source device", notes="Specify a source device Id to view all IP sessions, filtered by date")
	public SessionSummary[] getIpSessionSummariesBySrcDevice(
			@ApiParam(value="source deviceId", required = true)
			@PathParam("deviceid") long id,
			@ApiParam(value="start date", required = false)
			@QueryParam("startdate") String startDate,
			@ApiParam(value="end date", required = false)
			@QueryParam("enddate") String endDate) {
		return getIpSessionSummariesByDevice(id, startDate, endDate,
				SessionQueryType.SRC);
	}

	@GET
	@Path("/summary/device/dest/{deviceid}")
	@ApiOperation(value="Retrieve Ip session summaries for a dest device", notes="Specify a dest device Id to view all IP sessions, filtered by date")
	public SessionSummary[] getIpSessionSummariesByDestDevice(
			@ApiParam(value="dest deviceId", required = true)
			@PathParam("deviceid") long id,
			@ApiParam(value="start date", required = false)
			@QueryParam("startdate") String startDate,
			@ApiParam(value="end date", required = false)
			@QueryParam("enddate") String endDate) {
		return getIpSessionSummariesByDevice(id, startDate, endDate,
				SessionQueryType.DEST);
	}

	private SessionSummary[] getIpSessionSummariesByIpAddr(String ipAddr,
			String startDate, String endDate, SessionQueryType type) {
		long start = parseDateString(startDate, 0);
		long end = parseDateString(endDate, new Date().getTime());
		SessionSummary[] sessions = null;
		long qtime = new Date().getTime();
		if (ipAddr != null && !ipAddr.isEmpty()) {
			sessions = sessionService.getIpSessionSummariesByIpAddr(type,
					ipAddr, start, end);
			log.info(String
					.format("Retrieved %d sessions for address %s for date range %s (%d) to %s (%d) in %d ms",
							sessions.length, ipAddr, startDate, start, endDate,
							end, (new Date().getTime() - qtime)));
		}
		return sessions;
	}

	private SessionSummary[] getIpSessionSummariesByDevice(long id, String startDate,
			String endDate, SessionQueryType type) {
		long start = parseDateString(startDate, 0);
		long end = parseDateString(endDate, new Date().getTime());
		SessionSummary[] sessions = null;
		long qtime = new Date().getTime();
		sessions = sessionService.getIpSessionByDevice(type, id, start, end);
		log.info(String
				.format("Retrieved %d sessions for device %d for date range %s (%d) to %s (%d) in %d ms",
						sessions.length, id, startDate, start, endDate, end,
						(new Date().getTime() - qtime)));
		return sessions;

	}

	@GET
	@Path("/detail/device/{deviceid}")
	@ApiOperation(value="Retrieve Ip session details for a device", notes="Specify a deviceId to view all IP sessions, optionally filtered by protocol and date")
	public IpSession[] getIpSessionsByDevice(
			@ApiParam(value="deviceId", required = true)
			@PathParam("deviceid") long id,
			@ApiParam(value="protocol", required = false)
			@QueryParam("protocol") String protocol,
			@ApiParam(value="start date", required = false)
			@QueryParam("startdate") String startDate,
			@ApiParam(value="end date", required = false)
			@QueryParam("enddate") String endDate) {
		long start = parseDateString(startDate, 0);
		long end = parseDateString(endDate, new Date().getTime());
		IpSession[] sessions = null;
		sessions = sessionService.getDeviceIpSessions(id, -1, protocol, start, end);
		log.info(String
				.format("Retrieved %d sessions for device %d and protocol %s for date range %s (%d) to %s (%d)",
						sessions.length, id, protocol, startDate, start, endDate, end));
		return sessions;
	}

	@GET
	@Path("/detail/device/src/{deviceid}")
	@ApiOperation(value="Retrieve Ip session details for device(s)", notes="Specify a (source) deviceId and optionally a dest device id to view all IP sessions, optionally filtered by protocol and date")
	public IpSession[] getIpSessionsByDevices(
			@ApiParam(value="deviceId", required = true)
			@PathParam("deviceid") long id,
			@ApiParam(value="dest deviceId", required = true)
			@PathParam("destId") long destId,
			@ApiParam(value="protocol", required = false)
			@QueryParam("protocol") String protocol,
			@ApiParam(value="start date", required = false)
			@QueryParam("startdate") String startDate,
			@ApiParam(value="end date", required = false)
			@QueryParam("enddate") String endDate) {
		long start = parseDateString(startDate, 0);
		long end = parseDateString(endDate, new Date().getTime());
		IpSession[] sessions = null;
		sessions = sessionService.getDeviceIpSessions(id, destId, protocol, start, end);
		log.info(String
				.format("Retrieved %d sessions for device %d, destId %d and protocol %s for date range %s (%d) to %s (%d)",
						sessions.length, id, destId, protocol, startDate, start, endDate, end));
		return sessions;
	}

	@GET
	@Path("/detail/ipaddr/src/{ipaddr}")
	@ApiOperation(value="Retrieve Ip session details for a source and (optionally) dest Ip Addr", notes="Specify a source and (optionally) dest Ip addr to view all IP sessions, optionally filtered by protocol and date")
	public IpSession[] getIpSessionsByIpAddrs(
			@ApiParam(value="ipaddr", required = true)
			@PathParam("ipaddr") String ipAddr,
			@ApiParam(value="dest ipaddr", required = false)
			@QueryParam("destaddr") String destIpAddr,
			@ApiParam(value="protocol", required = false)
			@QueryParam("protocol") String protocol,
			@ApiParam(value="start date", required = false)
			@QueryParam("startdate") String startDate,
			@ApiParam(value="end date", required = false)
			@QueryParam("enddate") String endDate) {
		long start = parseDateString(startDate, 0);
		long end = parseDateString(endDate, new Date().getTime());
		IpSession[] sessions = null;
		if (ipAddr != null && !ipAddr.isEmpty()) {
			sessions = sessionService.getIpSessionsByIpAddr(ipAddr, destIpAddr, protocol, start, end);
			log.info(String
					.format("Retrieved %d sessions for address %s for date range %s (%d) to %s (%d)",
							sessions.length, ipAddr, startDate, start, endDate,
							end));
		}
		return sessions;
	}

	@GET
	@Path("/detail/ipaddr/{ipaddr}")
	@ApiOperation(value="Retrieve Ip session details for an Ip Addr", notes="Specify an Ip addr to view all IP sessions, optionally filtered by protocol and date")
	public IpSession[] getIpSessionsByIpAddr(
			@PathParam("ipaddr") String ipAddr,
			@ApiParam(value="protocol", required = false)
			@QueryParam("protocol") String protocol,
			@ApiParam(value="start date", required = false)
			@QueryParam("startdate") String startDate,
			@ApiParam(value="end date", required = false)
			@QueryParam("enddate") String endDate) {
		long start = parseDateString(startDate, 0);
		long end = parseDateString(endDate, new Date().getTime());
		IpSession[] sessions = null;
		if (ipAddr != null && !ipAddr.isEmpty()) {
			sessions = sessionService.getIpSessionsByIpAddr(ipAddr, null, protocol, start, end);
			log.info(String
					.format("Retrieved %d sessions for address %s for date range %s (%d) to %s (%d)",
							sessions.length, ipAddr, startDate, start, endDate,
							end));
		}
		return sessions;
	}
	
}
