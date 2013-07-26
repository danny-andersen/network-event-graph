package com.dsa.pcapneo.graph.repositories;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.cypherdsl.grammar.Execute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.annotation.QueryType;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.query.QueryEngine;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.HttpSession;
import com.dsa.pcapneo.domain.graph.IpSession;

@Component
public class SessionRepositoryImpl {
	@Autowired Neo4jTemplate template;
	@Autowired HttpSessionRepository httpRepo;
	@Autowired IpSessionRepository ipRepo;
	@Autowired SessionRepository repo;

	public List<IpSession> getIpSessionsByIpAddr(String ipAddr, long startTime, long endTime) {
		Iterable<IpSession> iter = ipRepo.getIpSessionsByIpAddr(ipAddr, startTime, endTime);
		List<IpSession> sessList = new ArrayList<IpSession>();
		for (IpSession session : iter) {
			sessList.add(session);
		}
		//Use CypherDSL
//		@Query ("start ipaddr=node:IpAddress(ipAddr:{0}) " +
//				"MATCH ipaddr-[:CONNECTS_FROM_IP|CONNECTS_TO_IP]-ip " +
//				"WHERE ip.startTime >= {1} AND ip.startTime <= {2} " +
//				"RETURN ip;")
//		QueryEngine<Map<String, Object>> engine = template.queryEngineFor(QueryType.Cypher);
//		Execute query = start(lookup());
		return sessList;
	}

	public List<IpSession> getIpSessionsByDeviceId(long deviceId, long startTime, long endTime) {
		Iterable<IpSession> iter = ipRepo.getIpSessionsByDevice(deviceId, startTime, endTime);
		List<IpSession> sessList = new ArrayList<IpSession>();
		for (IpSession session : iter) {
			sessList.add(session);
		}
		return sessList;
	}
	
	public List<HttpSession> getHttpSessionsByDeviceId(long deviceId, long startTime, long endTime) {
		Iterable<HttpSession> iter = httpRepo.getHttpSessionsByDevice(deviceId, startTime, endTime);
		List<HttpSession> sessList = new ArrayList<HttpSession>();
		for (HttpSession session : iter) {
			sessList.add(session);
		}
		return sessList;
	}


}
