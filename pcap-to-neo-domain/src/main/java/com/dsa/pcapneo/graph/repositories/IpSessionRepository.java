package com.dsa.pcapneo.graph.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.dsa.pcapneo.domain.graph.IpSession;

public interface IpSessionRepository extends GraphRepository<IpSession >{

}
