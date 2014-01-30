package com.dsa.pcapneo.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.sun.jersey.api.ParamException;
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
			@ApiParam(value="start date", required = true)
			@QueryParam("startdate") String startDate,
			@ApiParam(value="end date", required = true)
			@QueryParam("enddate") String endDate) {
		return getIpSessionSummaries(ipAddr, startDate, endDate,
				SessionQueryType.BOTH);
	}

	@GET
	@Path("/summary/ipaddr/src/{ipaddr}")
	public SessionSummary[] getIpSessionSummariesByIpAddrSrc(
			@PathParam("ipaddr") String ipAddr,
			@QueryParam("startdate") String startDate,
			@QueryParam("enddate") String endDate) {
		return getIpSessionSummaries(ipAddr, startDate, endDate,
				SessionQueryType.SRC);
	}

	@GET
	@Path("/summary/ipaddr/dest/{ipaddr}")
	public SessionSummary[] getIpSessionSummariesByIpAddrDest(
			@PathParam("ipaddr") String ipAddr,
			@QueryParam("startdate") String startDate,
			@QueryParam("enddate") String endDate) {
		return getIpSessionSummaries(ipAddr, startDate, endDate,
				SessionQueryType.DEST);
	}

	@GET
	@Path("/summary/device/{deviceid}")
	public SessionSummary[] getIpSessionSummariesByDevice(
			@PathParam("deviceid") long id,
			@QueryParam("startdate") String startDate,
			@QueryParam("enddate") String endDate) {
		return getIpSessionSummaries(id, startDate, endDate,
				SessionQueryType.BOTH);
	}

	@GET
	@Path("/summary/device/src/{deviceid}")
	public SessionSummary[] getIpSessionSummariesBySrcDevice(
			@PathParam("deviceid") long id,
			@QueryParam("startdate") String startDate,
			@QueryParam("enddate") String endDate) {
		return getIpSessionSummaries(id, startDate, endDate,
				SessionQueryType.SRC);
	}

	@GET
	@Path("/summary/device/dest/{deviceid}")
	public SessionSummary[] getIpSessionSummariesByDestDevice(
			@PathParam("deviceid") long id,
			@QueryParam("startdate") String startDate,
			@QueryParam("enddate") String endDate) {
		return getIpSessionSummaries(id, startDate, endDate,
				SessionQueryType.DEST);
	}

	private SessionSummary[] getIpSessionSummaries(String ipAddr,
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

	private SessionSummary[] getIpSessionSummaries(long id, String startDate,
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
	@Path("/detail/device/src/{deviceid}")
	public IpSession[] getIpSessionsByDevices(@PathParam("deviceid") long id,
			@QueryParam("destid") long destId,
			@QueryParam("startdate") String startDate,
			@QueryParam("enddate") String endDate) {
		long start = parseDateString(startDate, 0);
		long end = parseDateString(endDate, new Date().getTime());
		IpSession[] sessions = null;
		sessions = sessionService.getDeviceIpSessions(id, destId, start, end);
		log.info(String
				.format("Retrieved %d sessions for device %d for date range %s (%d) to %s (%d)",
						sessions.length, id, startDate, start, endDate, end));
		return sessions;
	}

	@GET
	@Path("/detail/ipaddr/src/{ipaddr}")
	public IpSession[] getIpSessionsByIpAddrs(
			@PathParam("ipaddr") String ipAddr,
			@QueryParam("destaddr") String destIpAddr,
			@QueryParam("startdate") String startDate,
			@QueryParam("enddate") String endDate) {
		long start = parseDateString(startDate, 0);
		long end = parseDateString(endDate, new Date().getTime());
		IpSession[] sessions = null;
		if (ipAddr != null && !ipAddr.isEmpty()) {
			sessions = sessionService.getIpSessionsByIpAddr(ipAddr, destIpAddr, start, end);
			log.info(String
					.format("Retrieved %d sessions for address %s for date range %s (%d) to %s (%d)",
							sessions.length, ipAddr, startDate, start, endDate,
							end));
		}
		return sessions;
	}

}
