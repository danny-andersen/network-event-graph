package com.dsa.pcapneo.graph.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.CypherDslRepository;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.dsa.pcapneo.domain.graph.IpSession;
import com.dsa.pcapneo.domain.graph.Port;

public interface IpSessionRepository extends GraphRepository<IpSession>, CypherDslRepository<IpSession> {

	@Query ("start device=node({0}) " +
			"MATCH device<-[:CONNECTS_FROM_DEVICE|CONNECTS_TO_DEVICE]-ip " +
			"WHERE ip.startTime >= {1} AND ip.startTime <= {2} AND" +
			"RETURN ip;")
	public Iterable<IpSession> getIpSessionsByDevice(long deviceId, long startTime, long endTime);

	@Query ("start device=node({0}) " +
			"MATCH device<-[:CONNECTS_FROM_DEVICE|CONNECTS_TO_DEVICE]-ip-[:CONNECTS_TO_DEVICE|CONNECTS_FROM_DEVICE]-destdev " +
			"WHERE ip.startTime >= {2} AND ip.startTime <= {3} AND destdev.id = {1}" +
			"RETURN ip;")
	public Iterable<IpSession> getIpSessionsByDeviceIds(long deviceId, long destId, long startTime, long endTime);

	@Query ("start ipaddr=node:IpAddress(ipAddr={0}) " +
			"MATCH ipaddr-[:CONNECTS_FROM_IP|CONNECTS_TO_IP]-ip " +
			"WHERE ip.startTime >= {1} AND ip.startTime <= {2} " +
			"RETURN ip;")
	public Iterable<IpSession> getIpSessionsByIpAddr(String ipaddr, long startTime, long endTime);

	@Query ("start ipaddr=node:IpAddress(ipAddr={0}) " +
			"MATCH ipaddr-[:CONNECTS_FROM_IP|CONNECTS_TO_IP]-ip-[:CONNECTS_TO_IP|CONNECTS_FROM_IP]-destIp " +
			"WHERE ip.startTime >= {2} AND ip.startTime <= {3} AND destIp.ipAddr = {1} " +
			"RETURN ip;")
	public Iterable<IpSession> getIpSessionsByIpAddr(String ipaddr, String destIp, long startTime, long endTime);
	
	@Query ("START port=node({0})" +
			"MATCH port-[:CONNECTS_FROM_PORT|CONNECTS_TO_PORT]-s " +
			"AND s.startTime >= {1} AND s.startTime <= {2} " +
			"RETURN s")
	public Iterable<IpSession> findSessionsByPort(Port port, long startTime, long endTime);

}
