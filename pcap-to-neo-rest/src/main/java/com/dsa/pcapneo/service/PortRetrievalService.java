package com.dsa.pcapneo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.Port;
import com.dsa.pcapneo.graph.repositories.PortRepository;

@Component
public class PortRetrievalService {
	@Autowired Neo4jTemplate template;
	@Autowired PortRepository repo;

	/**
	 * Find the ports that connect to the most devices
	 * @param minPort Find ports greater or equal to this
	 * @return List of ports, sorted by the most connected\
	 */
	public Port[] getMostConnectedPorts(int minPort) {
		Port[] ports = null;
		
		return ports;
	}

}
