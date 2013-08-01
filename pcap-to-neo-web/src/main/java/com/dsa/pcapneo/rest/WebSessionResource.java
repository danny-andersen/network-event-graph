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

import com.dsa.pcapneo.domain.graph.HttpSession;
import com.dsa.pcapneo.domain.graph.SessionSummary;
import com.dsa.pcapneo.graph.repositories.SessionRepository.SessionQueryType;
import com.dsa.pcapneo.service.SessionRetrievalService;
import com.sun.jersey.api.ParamException;

@Component
@Path("/session/web")
@Produces(MediaType.APPLICATION_JSON)
public class WebSessionResource {
	private static final Log log = LogFactory.getLog(WebSessionResource.class);
	public static final String DATE_FORMAT_LONG = "YYYY-MM-ddTHH:mm:ss";
	public static final String DATE_FORMAT_SHORT = "YYYY-MM-dd";
	@Autowired
	SessionRetrievalService sessionService;

	@GET
	@Path("/detail")
	public HttpSession[] getHttpSessions(@QueryParam("deviceid") long id,
			@QueryParam("ipaddr") String ipAddr,
			@QueryParam("startdate") String startDate,
			@QueryParam("enddate") String endDate) {
		long start = parseDateString(startDate, 0);
		long end = parseDateString(endDate, new Date().getTime());
		HttpSession[] sessions = null;
		if (ipAddr != null && !ipAddr.isEmpty()) {
		} else {
			sessions = sessionService.getDeviceHttpSessions(id, start, end);
			log.info(String
					.format("Retrieved %d sessions for device %d for date range %s (%d) to %s (%d)",
							sessions.length, id, startDate, start, endDate, end));
		}
		return sessions;
	}

	@GET
	@Path("/summary/device/{deviceid}")
	public SessionSummary[] getWebSessionSummaries(
			@PathParam("deviceid") long id,
			@QueryParam("startdate") String startDate,
			@QueryParam("enddate") String endDate) {
		long start = parseDateString(startDate, 0);
		long end = parseDateString(endDate, new Date().getTime());
		SessionSummary[] sessions = null;
		long qtime = new Date().getTime();
		sessions = sessionService
				.getWebSessionSummariesByDevice(SessionQueryType.BOTH, id, start, end);
		log.info(String
				.format("Retrieved %d web sessions for device %d for date range %s (%d) to %s (%d) in %d ms",
						sessions.length, id, startDate, start, endDate, end,
						(new Date().getTime() - qtime)));
		return sessions;
	}

	@GET
	@Path("/summary/device/src/{deviceid}")
	public SessionSummary[] getWebSessionSrcSummaries(
			@PathParam("deviceid") long id,
			@QueryParam("startdate") String startDate,
			@QueryParam("enddate") String endDate) {
		long start = parseDateString(startDate, 0);
		long end = parseDateString(endDate, new Date().getTime());
		SessionSummary[] sessions = null;
		long qtime = new Date().getTime();
			sessions = sessionService.getWebSessionSummariesByDevice(SessionQueryType.SRC, id, start,
					end);
			log.info(String
					.format("Retrieved %d web sessions for device %d for date range %s (%d) to %s (%d) in %d ms",
							sessions.length, id, startDate, start, endDate,
							end, (new Date().getTime() - qtime)));
		return sessions;
	}

	@GET
	@Path("/summary/device/dest/{deviceid}")
	public SessionSummary[] getWebSessionDestSummaries(
			@QueryParam("deviceid") long id,
			@QueryParam("startdate") String startDate,
			@QueryParam("enddate") String endDate) {
		long start = parseDateString(startDate, 0);
		long end = parseDateString(endDate, new Date().getTime());
		SessionSummary[] sessions = null;
		long qtime = new Date().getTime();
			sessions = sessionService.getWebSessionSummariesByDevice(SessionQueryType.DEST, id, start,
					end);
			log.info(String
					.format("Retrieved %d web sessions for device %d for date range %s (%d) to %s (%d) in %d ms",
							sessions.length, id, startDate, start, endDate,
							end, (new Date().getTime() - qtime)));
		return sessions;
	}

	@GET
	@Path("/summary/ipaddr/{ipaddr}")
	public SessionSummary[] getWebSessionSummariesByIpAddr(
			@PathParam("ipaddr") String ipAddr,
			@QueryParam("startdate") String startDate,
			@QueryParam("enddate") String endDate) {
		long start = parseDateString(startDate, 0);
		long end = parseDateString(endDate, new Date().getTime());
		SessionSummary[] sessions = null;
		long qtime = new Date().getTime();
		if (ipAddr != null && !ipAddr.isEmpty()) {
			sessions = sessionService.getWebSessionSummariesByIpAddr(SessionQueryType.BOTH, ipAddr,
					start, end);
			log.info(String
					.format("Retrieved %d web sessions for address %s for date range %s (%d) to %s (%d) in %d ms",
							sessions.length, ipAddr, startDate, start, endDate,
							end, (new Date().getTime() - qtime)));
		} 
		return sessions;
	}

	@GET
	@Path("/summary/ipaddr/src/{ipaddr}")
	public SessionSummary[] getWebSessionSummariesBySrcIpAddr(
			@PathParam("ipaddr") String ipAddr,
			@QueryParam("startdate") String startDate,
			@QueryParam("enddate") String endDate) {
		long start = parseDateString(startDate, 0);
		long end = parseDateString(endDate, new Date().getTime());
		SessionSummary[] sessions = null;
		long qtime = new Date().getTime();
		if (ipAddr != null && !ipAddr.isEmpty()) {
			sessions = sessionService.getWebSessionSummariesByIpAddr(SessionQueryType.SRC, ipAddr,
					start, end);
			log.info(String
					.format("Retrieved %d web sessions for address %s for date range %s (%d) to %s (%d) in %d ms",
							sessions.length, ipAddr, startDate, start, endDate,
							end, (new Date().getTime() - qtime)));
		} 
		return sessions;
	}

	@GET
	@Path("/summary/ipaddr/dest/{ipaddr}")
	public SessionSummary[] getWebSessionSummariesByDestIpAddr(
			@PathParam("ipaddr") String ipAddr,
			@QueryParam("startdate") String startDate,
			@QueryParam("enddate") String endDate) {
		long start = parseDateString(startDate, 0);
		long end = parseDateString(endDate, new Date().getTime());
		SessionSummary[] sessions = null;
		long qtime = new Date().getTime();
		if (ipAddr != null && !ipAddr.isEmpty()) {
			sessions = sessionService.getWebSessionSummariesByIpAddr(SessionQueryType.DEST, ipAddr,
					start, end);
			log.info(String
					.format("Retrieved %d web sessions for address %s for date range %s (%d) to %s (%d) in %d ms",
							sessions.length, ipAddr, startDate, start, endDate,
							end, (new Date().getTime() - qtime)));
		} 
		return sessions;
	}

	private long parseDateString(String startDate, long def) {
		long start = def;
		if (startDate != null && !startDate.isEmpty()) {
			SimpleDateFormat format = null;
			if (startDate.length() == DATE_FORMAT_LONG.length()) {
				format = new SimpleDateFormat(DATE_FORMAT_LONG);
			} else if (startDate.length() == DATE_FORMAT_SHORT.length()) {
				format = new SimpleDateFormat(DATE_FORMAT_SHORT);
			}
			if (format != null) {
				try {
					Date date = format.parse(startDate);
					start = date.getTime();
				} catch (ParseException pe) {
					log.error("Invalid date format: " + startDate);
					throw new ParamException.QueryParamException(pe, startDate,
							"Invalid date format");
				}
			}
		}
		return start;
	}
}
