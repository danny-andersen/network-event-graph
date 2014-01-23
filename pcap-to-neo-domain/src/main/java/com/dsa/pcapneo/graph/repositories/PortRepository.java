package com.dsa.pcapneo.graph.repositories;

import java.util.Map;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.dsa.pcapneo.domain.graph.Port;

public interface PortRepository extends GraphRepository<Port> {
	Iterable<Port> findByPort(int port);

	@Query ("START port=node:Port('port: *') " +
			"match port-[:CONNECTS_FROM_PORT|CONNECTS_TO_PORT]-s " +
			"RETURN port, count(s) as numSessions " +
			"ORDER BY numSessions DESC")
	Iterable<Map<String, Object>> findAllPortUsage();
	
	@Query ("START port=node:Port('port: *') " +
			"MATCH port-[:CONNECTS_FROM_PORT|CONNECTS_TO_PORT]-s " +
			"WHERE port.port >= {0} AND port.port <= {1} " +
			"RETURN port, count(s) as numSessions " +
			"ORDER BY numSessions DESC")
	Iterable<Map<String, Object>> findPortRangeUsage(int start, int end);
}
