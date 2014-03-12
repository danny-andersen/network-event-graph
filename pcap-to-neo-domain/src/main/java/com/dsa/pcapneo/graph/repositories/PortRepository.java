package com.dsa.pcapneo.graph.repositories;

import java.util.Map;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.dsa.pcapneo.domain.graph.Device;
import com.dsa.pcapneo.domain.graph.Port;

public interface PortRepository extends GraphRepository<Port> {
	public Iterable<Port> findByPort(int port);

	@Query ("START port=node:Port('port: *') " +
			"match port-[:CONNECTS_FROM_PORT|CONNECTS_TO_PORT]-s " +
			"RETURN port, count(s) as numSessions " +
			"ORDER BY numSessions DESC")
	public Iterable<Map<String, Object>> findAllPortUsage();
	
	@Query ("START port=node:Port('port: *') " +
			"MATCH port-[:CONNECTS_FROM_PORT|CONNECTS_TO_PORT]-s " +
			"WHERE port.port >= {0} AND port.port <= {1} " +
			"AND s.startTime >= {2} AND s.startTime <= {3} " +
			"RETURN port, count(s) as numSessions " +
			"ORDER BY numSessions DESC")
	public Iterable<Map<String, Object>> findPortRangeUsage(int start, int end, long startTime, long endTime);
	
	@Query ("START port=node:Port('port: *') " +
			"MATCH port-[:CONNECTS_FROM_PORT|CONNECTS_TO_PORT]-s-" +
			"[:CONNECTS_TO_DEVICE|CONNECTS_FROM_DEVICE]-d " +
			"WHERE port.port >= {0} AND port.port <= {1} " +
			"AND s.startTime >= {2} AND s.startTime <= {3} " +
			"RETURN port, COUNT(DISTINCT d) as numDevices " +
			"ORDER BY numDevices DESC")
	public Iterable<Map<String, Object>> findPortRangeDeviceUsage(int start, int end, long startTime, long endTime);

	@Query ("START port=node:Port('port: *') " +
			"MATCH port-[:CONNECTS_FROM_PORT|CONNECTS_TO_PORT]-s-" +
			"[:CONNECTS_TO_DEVICE|CONNECTS_FROM_DEVICE]-d " +
			"RETURN port, COUNT(DISTINCT d) as numDevices " +
			"ORDER BY numDevices DESC")
	public Iterable<Map<String, Object>> findAllPortDeviceUsage();

	@Query ("START dev=node({0}) " +
			"MATCH dev-[:CONNECTS_TO_DEVICE|CONNECTS_FROM_DEVICE]-s-" +
			"[:CONNECTS_FROM_PORT|CONNECTS_TO_PORT]-port " +
			"WHERE s.startTime >= {1} AND s.startTime <= {2} " +
			"RETURN port, COUNT(DISTINCT s) as numSessions " +
			"ORDER BY numSessions DESC")
	public Iterable<Map<String, Object>> findPortUsageOfDevice(Device device, long startTime, long endTime);
}
