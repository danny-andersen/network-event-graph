package com.dsa.pcapneo.service;

import org.springframework.stereotype.Component;

@SuppressWarnings("deprecation")
@Component
public class Neo4jWebService {
	/* This is was supposed to allow us to stand up a Neo server as way of browsing the graph but it is deprecated
	public WrappingNeoServerBootstrapper neoServer;
	public @Autowired Neo4jTemplate template;
	
	@PostConstruct
	public void init() {
		DelegatingGraphDatabase graphDb = (DelegatingGraphDatabase)template.getGraphDatabase();
		WrappingNeoServerBootstrapper neoServer = new WrappingNeoServerBootstrapper((GraphDatabaseAPI)graphDb.getGraphDatabaseService());
		neoServer.start();
	}
	
	@PreDestroy
	public void destroy() {
		neoServer.stop();
	}
	*/
}
