package com.dsa.pcapneo.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.dsa.pcapneo.domain.graph.HttpSession;
import com.dsa.pcapneo.domain.graph.IpSession;
import com.dsa.pcapneo.service.SessionRetrievalService;
import com.sun.jersey.api.ParamException;

@Component
@Path("/session")
@Produces(MediaType.APPLICATION_JSON)
public class SessionResource {
	private static final Log log = LogFactory.getLog(SessionResource.class);
	public static final String DATE_FORMAT_LONG = "YYYY-MM-ddTHH:mm:ss";
	public static final String DATE_FORMAT_SHORT = "YYYY-MM-dd";
	@Autowired SessionRetrievalService sessionService;
	
	@GET
	@Path("/ip")
	public IpSession[] getIpSessions(@QueryParam ("deviceid") long id,
							@QueryParam ("ipaddr") String ipAddr,
								@QueryParam("startdate") String startDate,
								@QueryParam("enddate") String endDate) {
		long start = parseDateString(startDate, 0);
		long end = parseDateString(endDate, new Date().getTime());
		IpSession[] sessions = null;
		if (ipAddr != null && !ipAddr.isEmpty()) {
			sessions = sessionService.getIpSessionsByIpAddr(ipAddr, start, end);
			log.info(String.format("Retrieved %d sessions for address %s for date range %s (%d) to %s (%d)",
								sessions.length, ipAddr, startDate, start, endDate, end));
		} else {
			sessions = sessionService.getDeviceIpSessions(id, start, end);
			log.info(String.format("Retrieved %d sessions for device %d for date range %s (%d) to %s (%d)",
								sessions.length, id, startDate, start, endDate, end));
		}
		return sessions;
	}

	@GET
	@Path("/web")
	public HttpSession[] getHttpSessionsByDeviceId(@QueryParam ("deviceId") long id,
								@QueryParam("startDate") String startDate,
								@QueryParam("endDate") String endDate) {
		long start = parseDateString(startDate, 0);
		long end = parseDateString(endDate, new Date().getTime());
		HttpSession[] sessions = sessionService.getDeviceHttpSessions(id, start, end);
		log.info(String.format("Retrieved %d sessions for device %d for date range %s to %s",sessions.length, id, startDate, endDate));
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
					throw new ParamException.QueryParamException(pe, startDate, "Invalid date format");
				}
			}
		}
		return start;
	}
}
