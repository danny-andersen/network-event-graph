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

@Component
@Path("/session/ip")
@Produces(MediaType.APPLICATION_JSON)
public class IpSessionResource {
	private static final Log log = LogFactory.getLog(IpSessionResource.class);
	public static final String DATE_FORMAT_LONG = "YYYY-MM-dd'T'HH:mm:ss";
	public static final String DATE_FORMAT_SHORT = "YYYY-MM-dd";
	@Autowired
	SessionRetrievalService sessionService;

	@GET
	@Path("/summary/ipaddr/{ipaddr}")
	public SessionSummary[] getIpSessionSummariesByIpAddrBoth(
			@PathParam("ipaddr") String ipAddr,
			@QueryParam("startdate") String startDate,
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

	private long parseDateString(String startDate, long def) {
		long start = def;
		//See if is a long date
		if (startDate != null && !startDate.isEmpty()) {
			try {
				Long.parseLong(startDate);
			} catch (NumberFormatException ne) {
			//Not a number
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
		}
		return start;
	}
}
