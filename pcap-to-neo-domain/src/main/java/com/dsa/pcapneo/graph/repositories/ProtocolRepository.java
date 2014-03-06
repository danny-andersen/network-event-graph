package com.dsa.pcapneo.graph.repositories;

import java.util.Map;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.Device;
import com.dsa.pcapneo.domain.graph.Port;
import com.dsa.pcapneo.domain.graph.Protocol;

@Component
public interface ProtocolRepository extends GraphRepository<Protocol>{
	public Iterable<Protocol> findProtocolByName(String name);
	
	@Query ("START proto=node:Protocol('name: *') " +
			"MATCH proto-[:VIA_PROTOCOL]-s " +
			"RETURN proto, count(s) as numSessions " +
			"ORDER BY numSessions DESC")
	public Iterable<Map<String, Object>> findAllProtocolsByUsage();

	@Query ("START proto=node:Protocol(name={0}) " +
			"MATCH proto-[:VIA_PROTOCOL]-s " +
			"WHERE s.startTime >= {1} AND s.startTime <= {2} " +
			"RETURN proto, count(s) as numSessions " +
			"ORDER BY numSessions DESC")
	public Iterable<Map<String, Object>> findProtocolUsage(String name, long startTime, long endTime);
	
	@Query ("START port=node({0}) " +
			"MATCH port-[:CONNECTS_FROM_PORT|CONNECTS_TO_PORT]-s-" +
			"[:VIA_PROTOCOL]-proto " +
			"WHERE s.startTime >= {1} AND s.startTime <= {2} " +
			"RETURN proto, count(s) as numSessions " +
			"ORDER BY numSessions DESC")
	public Iterable<Map<String, Object>> findProtocolUsageByPort(Port port, long startTime, long endTime);

	@Query ("START device=node({0}) " +
			"MATCH device-[:CONNECTS_FROM_DEVICE|CONNECTS_TO_DEVICE]-s-" +
			"[:VIA_PROTOCOL]-proto " +
			"WHERE s.startTime >= {1} AND s.startTime <= {2} " +
			"RETURN proto, count(s) as numSessions " +
			"ORDER BY numSessions DESC")
	public Iterable<Map<String, Object>> findProtocolUsageByDevice(Device device, long startTime, long endTime);

}
