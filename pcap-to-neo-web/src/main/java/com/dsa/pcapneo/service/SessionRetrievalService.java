package com.dsa.pcapneo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.HttpSession;
import com.dsa.pcapneo.domain.graph.IpSession;
import com.dsa.pcapneo.domain.graph.SessionSummary;
import com.dsa.pcapneo.graph.repositories.SessionRepository.SessionQueryType;
import com.dsa.pcapneo.graph.repositories.SessionRepoImpl;

@Component
public class SessionRetrievalService {
	@Autowired SessionRepoImpl repo;
	
	public HttpSession[] getDeviceHttpSessions(long deviceId, long startTime, long endTime) {
		HttpSession[] sessions = null;
		Iterable<HttpSession> iter = repo.getHttpSessionsByDeviceId(deviceId, startTime, endTime);
		List<HttpSession> sessList = new ArrayList<HttpSession>();
		for (HttpSession session : iter) {
			sessList.add(session);
		}
		sessions = sessList.toArray(new HttpSession[sessList.size()]);
		return sessions;
	}
	
	public IpSession[] getDeviceIpSessions(long deviceId, long destDev, long startTime, long endTime) {
		List<IpSession> sessList = repo.getIpSessionsByDeviceId(deviceId, destDev, startTime, endTime);
		IpSession[] sessions = sessList.toArray(new IpSession[sessList.size()]);
		return sessions;
	}

	public IpSession[] getIpSessionsByIpAddr(String ipAddr, String destIp, long startTime, long endTime) {
		List<IpSession> sessList = repo.getIpSessionsByIpAddr(ipAddr, destIp, startTime, endTime);
		IpSession[] sessions = sessList.toArray(new IpSession[sessList.size()]);
		return sessions;
	}

	public SessionSummary[] getIpSessionSummariesByIpAddr(SessionQueryType type, String ipAddr,
			long start, long end ) {
		List<SessionSummary> sums = null;
		switch (type) {
		case BOTH:
			sums = repo.getIpSessionSummaryByIpAddr(SessionQueryType.SRC, ipAddr, start, end);
			sums.addAll(repo.getIpSessionSummaryByIpAddr(SessionQueryType.DEST, ipAddr, start, end));
			break;
		default:
		case DEST:
		case SRC:
			sums = repo.getIpSessionSummaryByIpAddr(type, ipAddr, start, end);
			break;
		}
		SessionSummary[] ss = sums.toArray(new SessionSummary[sums.size()]);
		return ss;
	}

	public SessionSummary[] getIpSessionByDevice(SessionQueryType type, long id, long start, long end) {
		List<SessionSummary> sums = null;
		switch (type) {
		case BOTH:
			sums = repo.getIpSessionSummaryByDeviceId(SessionQueryType.SRC, id, start, end);
			sums.addAll(repo.getIpSessionSummaryByDeviceId(SessionQueryType.DEST, id, start, end));
			break;
		case SRC:
		case DEST:
		default:
			sums = repo.getIpSessionSummaryByDeviceId(type, id, start, end);
			break;
		}
		SessionSummary[] ss = sums.toArray(new SessionSummary[sums.size()]);
		return ss;
	}

	public SessionSummary[] getWebSessionSummariesByIpAddr(SessionQueryType type, String ipAddr,
			long start, long end) {
		List<SessionSummary> sums = null;
		switch (type) {
		case BOTH:
			sums = repo.getWebSessionSummaryByIpAddr(SessionQueryType.SRC, ipAddr, start, end);
			sums.addAll(repo.getWebSessionSummaryByIpAddr(SessionQueryType.DEST, ipAddr, start, end));
			break;
		default:
		case DEST:
		case SRC:
			sums = repo.getWebSessionSummaryByIpAddr(type, ipAddr, start, end);
			break;
		}
		SessionSummary[] ss = sums.toArray(new SessionSummary[sums.size()]);
		return ss;
	}

	public SessionSummary[] getWebSessionSummariesByDevice(SessionQueryType type, long id, long start,
			long end) {
		List<SessionSummary> sums = null;
		switch (type) {
		case BOTH:
			sums = repo.getWebSessionSummaryByDeviceId(SessionQueryType.SRC, id, start, end);
			sums.addAll(repo.getWebSessionSummaryByDeviceId(SessionQueryType.DEST, id, start, end));
			break;
		default:
		case DEST:
		case SRC:
			sums = repo.getWebSessionSummaryByDeviceId(type, id, start, end);
			break;
		}
		SessionSummary[] ss = sums.toArray(new SessionSummary[sums.size()]);
		return ss;
	}
}
