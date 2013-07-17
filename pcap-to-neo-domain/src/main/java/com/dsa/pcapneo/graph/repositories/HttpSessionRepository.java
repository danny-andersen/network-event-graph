package com.dsa.pcapneo.graph.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.dsa.pcapneo.domain.graph.HttpSession;

public interface HttpSessionRepository extends GraphRepository<HttpSession> {
	@Query ("start device=node({0}) " +
			"MATCH device<-[:CONNECTS_FROM_DEVICE]-http " +
			"WHERE http.startTime >= {1} AND http.startTime <= {2} " +
			"RETURN http;")
	public Iterable<HttpSession> getHttpSessionsByDevice(long deviceId, long startTime, long endTime);
}
