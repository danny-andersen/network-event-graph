package com.dsa.pcapneo.service;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocalGraphDatabase {
	@Autowired
	private GraphDatabaseService graphDb;

	// @Autowired
	// private String dbPath;
	//
	// @Autowired
	// private String dbPropertiesFile;
	//
	public void startGraphDb() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}

	public void close() {
		this.graphDb.shutdown();
	}

}
