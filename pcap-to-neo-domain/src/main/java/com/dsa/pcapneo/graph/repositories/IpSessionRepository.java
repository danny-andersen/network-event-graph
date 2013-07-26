package com.dsa.pcapneo.graph.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.CypherDslRepository;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.dsa.pcapneo.domain.graph.IpSession;

public interface IpSessionRepository extends GraphRepository<IpSession>, CypherDslRepository<IpSession> {

	@Query ("start device=node({0}) " +
			"MATCH device<-[:CONNECTS_FROM_DEVICE]-ip " +
			"WHERE ip.startTime >= {1} AND ip.startTime <= {2} " +
			"RETURN ip;")
	public Iterable<IpSession> getIpSessionsByDevice(long deviceId, long startTime, long endTime);

	@Query ("start ipaddr=node:IpAddress(ipAddr={0}) " +
			"MATCH ipaddr-[:CONNECTS_FROM_IP|CONNECTS_TO_IP]-ip " +
			"WHERE ip.startTime >= {1} AND ip.startTime <= {2} " +
			"RETURN ip;")
	public Iterable<IpSession> getIpSessionsByIpAddr(String ipaddr, long startTime, long endTime);
}
