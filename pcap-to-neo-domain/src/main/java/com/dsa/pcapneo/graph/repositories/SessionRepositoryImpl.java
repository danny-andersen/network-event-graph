package com.dsa.pcapneo.graph.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.annotation.QueryType;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.query.QueryEngine;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.Device;
import com.dsa.pcapneo.domain.graph.HttpSession;
import com.dsa.pcapneo.domain.graph.IpSession;
import com.dsa.pcapneo.domain.graph.SessionSummary;

@Component
public class SessionRepositoryImpl {
	@Autowired Neo4jTemplate template;
	@Autowired HttpSessionRepository httpRepo;
	@Autowired IpSessionRepository ipRepo;
	@Autowired SessionRepository repo;
	private static final String IP_SESSION_SUMMARY_BY_IPADDR_SRC = "start ip=node:IpAddress(ipAddr={ipAddr}) " + 
			"MATCH ip-[:CONNECTS_FROM_IP]-ips-[:CONNECTS_TO_IP]-ipdest " +
			"WHERE ips.startTime >= {startTime} AND ips.startTime <= {endTime} " +
			"RETURN ipdest.ipAddr as ipDest, count(*) as numSessions, max(ips.startTime) as lastTime " + 
			"ORDER BY ipdest.ipAddr;";            

	private static final String IP_SESSION_SUMMARY_BY_DEVICE_SRC = "start dev=node:Device({deviceId}) " + 
			"MATCH dev-[:CONNECTS_FROM_DEVICE]-ips-[:CONNECTS_TO_DEVICE]-devdest " +
			"WHERE ips.startTime >= {startTime} AND ips.startTime <= {endTime} " +
			"RETURN devdest.hostName as hostName, count(*) as numSessions, max(ips.startTime) as lastTime " + 
			"ORDER BY devdest.hostName;";            

	private static final String WEB_SESSION_SUMMARY_BY_IPADDR_SRC = "start ip=node:IpAddress(ipAddr={ipAddr}) " + 
			"MATCH dev-[:CONNECTS_USING]->ip-[:CONNECTS_FROM_IP]-hps-[:CONNECTS_TO]-web " +
			"WHERE hps.startTime >= {startTime} AND hps.startTime <= {endTime} " +
			"RETURN dev as device, web.address as webSite, count(*) as numSessions, max(hps.startTime) as lastTime " + 
			"ORDER BY web.address;";


	public List<IpSession> getIpSessionsByIpAddr(String ipAddr, long startTime, long endTime) {
		Iterable<IpSession> iter = ipRepo.getIpSessionsByIpAddr(ipAddr, startTime, endTime);
		List<IpSession> sessList = new ArrayList<IpSession>();
		for (IpSession session : iter) {
			sessList.add(session);
		}
		return sessList;
	}

	public List<SessionSummary> getIpSessionSummaryByIpAddr(String ipAddr, long startTime, long endTime) {
		QueryEngine<Map<String, Object>> engine = template.queryEngineFor(QueryType.Cypher);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ipAddr", ipAddr);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		Result<Map<String, Object>> result = engine.query(IP_SESSION_SUMMARY_BY_IPADDR_SRC, params);
		List<SessionSummary> sess = new ArrayList<SessionSummary>();
		for (Map<String, Object> map: result) {
			SessionSummary s = new SessionSummary();
			s.setSrcIpAddr(ipAddr);
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (entry.getKey().compareTo("ipDest") == 0) {
					s.setDestIpAddr((String)entry.getValue());
				} else if (entry.getKey().compareTo("numSessions") == 0) {
					s.setNumSessions((Long)entry.getValue());
				} else if (entry.getKey().compareTo("lastTime") == 0) {
					s.setLatest((Long)entry.getValue());
				}
			}
			sess.add(s);
		}
		return sess;
	}
	
	public List<IpSession> getIpSessionsByDeviceId(long deviceId, long startTime, long endTime) {
		Iterable<IpSession> iter = ipRepo.getIpSessionsByDevice(deviceId, startTime, endTime);
		List<IpSession> sessList = new ArrayList<IpSession>();
		for (IpSession session : iter) {
			sessList.add(session);
		}
		return sessList;
	}
	
	public List<SessionSummary> getIpSessionSummaryByDeviceId(long deviceId, long startTime, long endTime) {
		QueryEngine<Map<String, Object>> engine = template.queryEngineFor(QueryType.Cypher);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceId", deviceId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		Result<Map<String, Object>> result = engine.query(IP_SESSION_SUMMARY_BY_DEVICE_SRC, params);
		List<SessionSummary> sess = new ArrayList<SessionSummary>();
		for (Map<String, Object> map: result) {
			SessionSummary s = new SessionSummary();
			s.setSrcDeviceId(deviceId);
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (entry.getKey().compareTo("ipDest") == 0) {
					s.setDestDeviceId((Long)entry.getValue());
				} else if (entry.getKey().compareTo("numSessions") == 0) {
					s.setNumSessions((Long)entry.getValue());
				} else if (entry.getKey().compareTo("lastTime") == 0) {
					s.setLatest((Long)entry.getValue());
				}
			}
			sess.add(s);
		}
		return sess;
	}

	public List<HttpSession> getHttpSessionsByDeviceId(long deviceId, long startTime, long endTime) {
		Iterable<HttpSession> iter = httpRepo.getHttpSessionsByDevice(deviceId, startTime, endTime);
		List<HttpSession> sessList = new ArrayList<HttpSession>();
		for (HttpSession session : iter) {
			sessList.add(session);
		}
		return sessList;
	}

	public List<HttpSession> getHttpSessionsByIpAddr(String ipAddr, long startTime, long endTime) {
		Iterable<HttpSession> iter = httpRepo.getHttpSessionsByIpAddr(ipAddr, startTime, endTime);
		List<HttpSession> sessList = new ArrayList<HttpSession>();
		for (HttpSession session : iter) {
			sessList.add(session);
		}
		return sessList;
	}

	public List<SessionSummary> getWebSessionSummaryByIpAddr(String ipAddr,
			long startTime, long endTime) {
		QueryEngine<Map<String, Object>> engine = template.queryEngineFor(QueryType.Cypher);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ipAddr", ipAddr);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		Result<Map<String, Object>> result = engine.query(WEB_SESSION_SUMMARY_BY_IPADDR_SRC, params);
		List<SessionSummary> sess = new ArrayList<SessionSummary>();
		for (Map<String, Object> map: result) {
			SessionSummary s = new SessionSummary();
			s.setSrcIpAddr(ipAddr);
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (entry.getKey().compareTo("webSite") == 0) {
					s.setWebAddress((String)entry.getValue());
				} else if (entry.getKey().compareTo("numSessions") == 0) {
					s.setNumSessions((Long)entry.getValue());
				} else if (entry.getKey().compareTo("lastTime") == 0) {
					s.setLatest((Long)entry.getValue());
				} else if (entry.getKey().compareTo("device") == 0) {
					s.setSrcDeviceId(template.convert(entry.getValue(), Device.class).getDeviceId());
				}
			}
			sess.add(s);
		}
		return sess;
	}

}
