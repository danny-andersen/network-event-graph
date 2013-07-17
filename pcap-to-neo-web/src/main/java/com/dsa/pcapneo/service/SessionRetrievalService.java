package com.dsa.pcapneo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;

@Component
public class SessionRetrievalService {
	@Autowired Neo4jTemplate template;
//	@Autowired HttpSessionRepository httpRepo;

}
