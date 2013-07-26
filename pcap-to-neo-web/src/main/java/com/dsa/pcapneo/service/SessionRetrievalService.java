package com.dsa.pcapneo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.HttpSession;
import com.dsa.pcapneo.domain.graph.IpSession;
import com.dsa.pcapneo.graph.repositories.SessionRepositoryImpl;

@Component
public class SessionRetrievalService {
	@Autowired SessionRepositoryImpl repo;

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
	
	public IpSession[] getDeviceIpSessions(long deviceId, long startTime, long endTime) {
		List<IpSession> sessList = repo.getIpSessionsByDeviceId(deviceId, startTime, endTime);
		IpSession[] sessions = sessList.toArray(new HttpSession[sessList.size()]);
		return sessions;
	}

	public IpSession[] getIpSessionsByIpAddr(String ipAddr, long startTime, long endTime) {
		List<IpSession> sessList = repo.getIpSessionsByIpAddr(ipAddr, startTime, endTime);
		IpSession[] sessions = sessList.toArray(new IpSession[sessList.size()]);
		return sessions;
	}
}
