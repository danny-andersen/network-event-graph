package com.dsa.pcapneo.graph.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.dsa.pcapneo.domain.graph.WebSite;

public interface WebSiteRepository extends GraphRepository<WebSite> {
	Iterable<WebSite> findByAddressLike(String address);

//	Iterable<WebSite> findAllByQuery(String index, String field, String query);
}
