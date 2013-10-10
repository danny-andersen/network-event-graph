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
import com.dsa.pcapneo.graph.repositories.SessionRepository.SessionQueryType;

@Component
public class SessionRepositoryImpl {
	@Autowired Neo4jTemplate template;
	@Autowired HttpSessionRepository httpRepo;
	@Autowired IpSessionRepository ipRepo;
	@Autowired SessionRepository repo;


	public List<IpSession> getIpSessionsByIpAddr(String ipAddr, String destIp, long startTime, long endTime) {
		Iterable<IpSession> iter;
		if (destIp != null && !destIp.isEmpty()) {
			iter = ipRepo.getIpSessionsByIpAddr(ipAddr, destIp, startTime, endTime);
		} else {
			iter = ipRepo.getIpSessionsByIpAddr(ipAddr, startTime, endTime);
		}
		List<IpSession> sessList = new ArrayList<IpSession>();
		for (IpSession session : iter) {
			sessList.add(session);
		}
		return sessList;
	}

	private static final String IP_SESSION_SUMMARY_BY_IPADDR_SRC = "start ipsrc=node:IpAddress(ipAddr={ipAddr}) " + 
			"MATCH ipsrc-[:CONNECTS_FROM_IP]-ips-[:CONNECTS_TO_IP]-ipdest " +
			"WHERE ips.startTime >= {startTime} AND ips.startTime <= {endTime} " +
			"RETURN ipsrc.ipAddr as ipSrc, ipdest.ipAddr as ipDest, count(*) as numSessions, min(ips.startTime) as earliest, max(ips.startTime) as lastTime " + 
			"ORDER BY ipdest.ipAddr;";            

	private static final String IP_SESSION_SUMMARY_BY_IPADDR_DEST = "start ipdest=node:IpAddress(ipAddr={ipAddr}) " + 
			"MATCH ipsrc-[:CONNECTS_FROM_IP]-ips-[:CONNECTS_TO_IP]-ipdest " +
			"WHERE ips.startTime >= {startTime} AND ips.startTime <= {endTime} " +
			"RETURN ipdest.ipAddr as ipDest, ipsrc.ipAddr as ipSrc, count(*) as numSessions, min(ips.startTime) as earliest, max(ips.startTime) as lastTime " + 
			"ORDER BY ipsrc.ipAddr;";            

	public List<SessionSummary> getIpSessionSummaryByIpAddr(SessionQueryType type, String ipAddr, long startTime, long endTime) {
		QueryEngine<Map<String, Object>> engine = template.queryEngineFor(QueryType.Cypher);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ipAddr", ipAddr);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		Result<Map<String, Object>> result;
		switch (type) {
		case SRC:
			result = engine.query(IP_SESSION_SUMMARY_BY_IPADDR_SRC, params);
			break;
		case DEST:
		default:
			result = engine.query(IP_SESSION_SUMMARY_BY_IPADDR_DEST, params);
			break;
		}
		return processResultSet(result);
	}
	
	public List<IpSession> getIpSessionsByDeviceId(long deviceId, long destId, long startTime, long endTime) {
		Iterable<IpSession> iter;
		if (destId != -1 || destId != 0) {
			iter = ipRepo.getIpSessionsByDeviceIds(deviceId, destId, startTime, endTime);
		} else {
			iter = ipRepo.getIpSessionsByDevice(deviceId, startTime, endTime);
		}
		List<IpSession> sessList = new ArrayList<IpSession>();
		for (IpSession session : iter) {
			sessList.add(session);
		}
		return sessList;
	}
	
	private static final String IP_SESSION_SUMMARY_BY_DEVICE_SRC = "start dev=node({deviceId}) " + 
			"MATCH dev-[:CONNECTS_FROM_DEVICE]-ips-[:CONNECTS_TO_DEVICE]-devdest " +
			"WHERE ips.startTime >= {startTime} AND ips.startTime <= {endTime} " +
			"RETURN dev as devsrc, devdest, count(*) as numSessions, max(ips.startTime) as lastTime " + 
			"ORDER BY devdest.hostName;";            

	private static final String IP_SESSION_SUMMARY_BY_DEVICE_DEST = "start devdest=node({deviceId}) " + 
			"MATCH devsrc-[:CONNECTS_FROM_DEVICE]-ips-[:CONNECTS_TO_DEVICE]-devdest " +
			"WHERE ips.startTime >= {startTime} AND ips.startTime <= {endTime} " +
			"RETURN devsrc, devdest, count(*) as numSessions, max(ips.startTime) as lastTime " + 
			"ORDER BY devdest.hostName;";            

	public List<SessionSummary> getIpSessionSummaryByDeviceId(SessionQueryType type, long deviceId, long startTime, long endTime) {
		QueryEngine<Map<String, Object>> engine = template.queryEngineFor(QueryType.Cypher);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceId", deviceId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		Result<Map<String, Object>> result = null;
		switch (type) {
		case SRC:
			result = engine.query(IP_SESSION_SUMMARY_BY_DEVICE_SRC, params);
			break;
		case DEST:
		default:
			result = engine.query(IP_SESSION_SUMMARY_BY_DEVICE_DEST, params);
		}
		return processResultSet(result);
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

	private static final String WEB_SESSION_SUMMARY_BY_IPADDR_SRC = "start ip=node:IpAddress(ipAddr={ipAddr}) " + 
			"MATCH dev-[:CONNECTS_USING]->ip-[:CONNECTS_FROM_IP]-hps-[:CONNECTS_TO]-web " +
			"WHERE hps.startTime >= {startTime} AND hps.startTime <= {endTime} " +
			"RETURN dev as devsrc, web.address as webSite, count(*) as numSessions, max(hps.startTime) as lastTime " + 
			"ORDER BY web.address;";

	private static final String WEB_SESSION_SUMMARY_BY_IPADDR_DEST = "start ip=node:IpAddress(ipAddr={ipAddr}) " + 
			"MATCH dev-[:CONNECTS_USING]->ip-[:CONNECTS_TO_IP]-hps-[:CONNECTS_TO]-web " +
			"WHERE hps.startTime >= {startTime} AND hps.startTime <= {endTime} " +
			"RETURN dev as devdest, web.address as webSite, count(*) as numSessions, max(hps.startTime) as lastTime " + 
			"ORDER BY web.address;";

	public List<SessionSummary> getWebSessionSummaryByIpAddr(SessionQueryType type, String ipAddr,
			long startTime, long endTime) {
		QueryEngine<Map<String, Object>> engine = template.queryEngineFor(QueryType.Cypher);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ipAddr", ipAddr);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		Result<Map<String, Object>> result;
		switch (type) {
		case SRC:
			result = engine.query(WEB_SESSION_SUMMARY_BY_IPADDR_SRC, params);
			break;
		case DEST:
		default:
			result = engine.query(WEB_SESSION_SUMMARY_BY_IPADDR_DEST, params);
			break;
		}
		return processResultSet(result);
	}

	private static final String WEB_SESSION_SUMMARY_BY_DEVICE_SRC = "start dev=node({deviceId}) " + 
			"MATCH dev-[:CONNECTS_FROM_DEVICE]-hps-[:CONNECTS_TO]-web " +
			"WHERE hps.startTime >= {startTime} AND hps.startTime <= {endTime} " +
			"RETURN dev as devsrc, web.address as webSite, count(*) as numSessions, max(hps.startTime) as lastTime " + 
			"ORDER BY web.address;";

	private static final String WEB_SESSION_SUMMARY_BY_DEVICE_DEST = "start dev=node({deviceId}) " + 
			"MATCH dev-[:CONNECTS_TO_DEVICE]-hps-[:CONNECTS_FROM]-web " +
			"WHERE hps.startTime >= {startTime} AND hps.startTime <= {endTime} " +
			"RETURN dev as devdest, web.address as webSite, count(*) as numSessions, max(hps.startTime) as lastTime " + 
			"ORDER BY web.address;";

	public List<SessionSummary> getWebSessionSummaryByDeviceId(SessionQueryType type, long deviceId,
			long startTime, long endTime) {
		QueryEngine<Map<String, Object>> engine = template.queryEngineFor(QueryType.Cypher);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceId", deviceId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		Result<Map<String, Object>> result;
		switch(type) {
		case SRC:
			result = engine.query(WEB_SESSION_SUMMARY_BY_DEVICE_SRC, params);
			break;
		case DEST:
		default:
			result = engine.query(WEB_SESSION_SUMMARY_BY_DEVICE_DEST, params);
			break;
		}
		return processResultSet(result);
	}
	
	private List<SessionSummary> processResultSet(Result<Map<String, Object>> result) {
		List<SessionSummary> sess = new ArrayList<SessionSummary>();
		for (Map<String, Object> map: result) {
			SessionSummary s = new SessionSummary();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (entry.getKey().compareTo("devsrc") == 0) {
					Device dev = template.convert(entry.getValue(), Device.class);
					s.setSrcDeviceId(dev.getDeviceId());
					s.setSrcHostname(dev.getHostName());
				} else if (entry.getKey().compareTo("devdest") == 0) {
					Device dev = template.convert(entry.getValue(), Device.class);
					s.setDestDeviceId(dev.getDeviceId());
					s.setDestHostname(dev.getHostName());
				} else if (entry.getKey().compareTo("webSite") == 0) {
					s.setWebAddress((String)entry.getValue());
				} else if (entry.getKey().compareTo("ipDest") == 0) {
					s.setDestIpAddr((String)entry.getValue());
				} else if (entry.getKey().compareTo("ipSrc") == 0) {
					s.setSrcIpAddr((String)entry.getValue());
				} else if (entry.getKey().compareTo("numSessions") == 0) {
					s.setNumSessions((Long)entry.getValue());
				} else if (entry.getKey().compareTo("earliest") == 0) {
					s.setEarliest((Long)entry.getValue());
				} else if (entry.getKey().compareTo("lastTime") == 0) {
					s.setLatest((Long)entry.getValue());
				}
			}
			sess.add(s);
		}
		return sess;
	}
}
