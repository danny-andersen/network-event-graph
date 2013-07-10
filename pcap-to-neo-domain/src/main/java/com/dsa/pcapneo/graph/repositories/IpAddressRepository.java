package com.dsa.pcapneo.graph.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.dsa.pcapneo.domain.graph.IpAddress;

public interface IpAddressRepository extends GraphRepository<IpAddress> {
	Iterable<IpAddress> findByIpAddrLike(String ipaddr);
}
