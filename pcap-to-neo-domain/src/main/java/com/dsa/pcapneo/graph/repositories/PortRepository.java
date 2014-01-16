package com.dsa.pcapneo.graph.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.dsa.pcapneo.domain.graph.Port;

public interface PortRepository extends GraphRepository<Port> {
	Iterable<Port> findByPort(int port);
}
