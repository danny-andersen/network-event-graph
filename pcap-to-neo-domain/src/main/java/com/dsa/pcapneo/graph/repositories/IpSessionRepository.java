package com.dsa.pcapneo.graph.repositories;

import java.util.Map;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.CypherDslRepository;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.dsa.pcapneo.domain.graph.IpSession;
import com.dsa.pcapneo.domain.graph.Port;

public interface IpSessionRepository extends GraphRepository<IpSession>, CypherDslRepository<IpSession> {

	@Query ("start device=node({0}) " +
			"MATCH device-[:CONNECTS_FROM_DEVICE|CONNECTS_TO_DEVICE]-ip " +
			"WHERE ip.startTime >= {1} AND ip.startTime <= {2} " +
			"RETURN ip;")
	public Iterable<IpSession> getIpSessionsByDevice(long deviceId, long startTime, long endTime);

	@Query ("start device=node({0}) " +
			"MATCH device-[:CONNECTS_FROM_DEVICE|CONNECTS_TO_DEVICE]-ip-" +
			"[:VIA_PROTOCOL]-protocol " +
			"WHERE ip.startTime >= {2} AND ip.startTime <= {3} " +
			"AND protocol.name =~ {1} " +
			"RETURN ip;")
	public Iterable<IpSession> getIpSessionsByDeviceAndProtocol(long deviceId, String protocol, long startTime, long endTime);

	@Query ("start device=node({0}) " +
			"MATCH device-[:CONNECTS_FROM_DEVICE|CONNECTS_TO_DEVICE]-ip-" +
			"[:CONNECTS_FROM_PORT|CONNECTS_TO_PORT]-port " +
			"WHERE ip.startTime >= {2} AND ip.startTime <= {3} " +
			"AND port.port = {1} " +
			"RETURN ip;")
	public Iterable<IpSession> getIpSessionsByDeviceAndPort(long deviceId, int port, long startTime, long endTime);

	@Query ("start device=node({0}), destdev=node({1}) " +
			"MATCH device-[:CONNECTS_FROM_DEVICE]-ip-[:CONNECTS_TO_DEVICE]-destdev " +
			"WHERE ip.startTime >= {2} AND ip.startTime <= {3} " +
			"RETURN ip;")
	public Iterable<IpSession> getIpSessionsByDeviceIds(long deviceId, long destId, long startTime, long endTime);

	@Query ("start device=node({0}), destdev=node({1}) " +
			"MATCH device-[:CONNECTS_FROM_DEVICE]-ip-[:CONNECTS_TO_DEVICE]-destdev, " +
			"ip-[:VIA_PROTOCOL]-protocol " +
			"WHERE ip.startTime >= {3} AND ip.startTime <= {4} AND protocol.name=~{2} " +
			"RETURN ip;")
	public Iterable<IpSession> getIpSessionsByDeviceIdsAndProtocol(long deviceId, long destId, String protocol, long startTime, long endTime);

	@Query ("start device=node({0}), destdev=node({1}) " +
			"MATCH device-[:CONNECTS_FROM_DEVICE]-ip-[:CONNECTS_TO_DEVICE]-destdev, " +
			"ip-[:CONNECTS_FROM_PORT|CONNECTS_TO_PORT]-port " +
			"WHERE ip.startTime >= {3} AND ip.startTime <= {4} " +
			"AND port.port = {2} " +
			"RETURN ip;")
	public Iterable<IpSession> getIpSessionsByDeviceIdsAndPort(long deviceId, long destId, int port, long startTime, long endTime);

	@Query ("start ipaddr=node:IpAddress(ipAddr={0}) " +
			"MATCH ipaddr-[:CONNECTS_FROM_IP|CONNECTS_TO_IP]-ip " +
			"WHERE ip.startTime >= {1} AND ip.startTime <= {2} " +
			"RETURN ip;")
	public Iterable<IpSession> getIpSessionsByIpAddr(String ipaddr, long startTime, long endTime);

	@Query ("start ipaddr=node:IpAddress(ipAddr={0}) " +
			"MATCH ipaddr-[:CONNECTS_FROM_IP|CONNECTS_TO_IP]-ip-[:VIA_PROTOCOL]-protocol " +
			"WHERE ip.startTime >= {2} AND ip.startTime <= {3} AND protocol.name=~{1} " +
			"RETURN ip;")
	public Iterable<IpSession> getIpSessionsByIpAddrAndProtocol(String ipaddr, String protocol, long startTime, long endTime);

	@Query ("start ipaddr=node:IpAddress(ipAddr={0}) " +
			"MATCH ipaddr-[:CONNECTS_FROM_IP|CONNECTS_TO_IP]-ip-[:CONNECTS_TO_IP|CONNECTS_FROM_IP]-destIp " +
			"WHERE ip.startTime >= {2} AND ip.startTime <= {3} AND destIp.ipAddr = {1} " +
			"RETURN ip;")
	public Iterable<IpSession> getIpSessionsByIpAddrs(String ipaddr, String destIp, long startTime, long endTime);
	
	@Query ("start ipaddr=node:IpAddress(ipAddr={0}) " +
			"MATCH ipaddr-[:CONNECTS_FROM_IP|CONNECTS_TO_IP]-ip-[:CONNECTS_TO_IP|CONNECTS_FROM_IP]-destIp, " +
			"ip-[:VIA_PROTOCOL]-protocol " +
			"WHERE ip.startTime >= {3} AND ip.startTime <= {4} AND destIp.ipAddr = {1} AND protocol.name=~{2} " +
			"RETURN ip;")
	public Iterable<IpSession> getIpSessionsByIpAddrsAndProtocol(String ipaddr, String destIp, String protocol, long startTime, long endTime);

	@Query ("start port=node({0}) " +
			"MATCH port-[:CONNECTS_FROM_PORT|CONNECTS_TO_PORT]-ip " +
			"WHERE ip.startTime >= {1} AND ip.startTime <= {2} " +
			"RETURN ip;")
	public Iterable<IpSession> getIpSessionsByPort(Port port, long startTime, long endTime);

	@Query ("start port=node({0}) " +
			"MATCH port-[:CONNECT_FROM_PORT|CONNECTS_TO_PORT]-ip-[:VIA_PROTOCOL]-protocol " +
			"WHERE ip.startTime >= {2} AND ip.startTime <= {3} " +
			"AND protocol.name=~{1} " +
			"RETURN ip;")
	public Iterable<IpSession> getIpSessionsByPortAndProtocol(Port port, String protocol, long startTime, long endTime);


}
