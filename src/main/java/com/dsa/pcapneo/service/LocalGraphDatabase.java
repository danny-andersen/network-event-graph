package com.dsa.pcapneo.service;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class LocalGraphDatabase {
	private GraphDatabaseService graphDb;

	@Autowired
	private GraphDatabaseFactory graphDbFactory;

	@Autowired
	private String dbPath;

	@Autowired
	private String dbPropertiesFile;

	public GraphDatabaseService open() {
		if (this.graphDb == null) {
			GraphDatabaseBuilder builder = graphDbFactory.newEmbeddedDatabaseBuilder(dbPath);
			builder.loadPropertiesFromFile(dbPropertiesFile);
			final GraphDatabaseService db = builder.newGraphDatabase();
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					db.shutdown();
				}
			});
			this.graphDb = db;
		}
		return this.graphDb;
	}

	public void close() {
		this.graphDb.shutdown();
	}

	public void setGraphDbFactory(GraphDatabaseFactory graphDbFactory) {
		this.graphDbFactory = graphDbFactory;
	}

	public String getDbPath() {
		return dbPath;
	}

	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}

	public void setDbPropertiesFile(String path) {
		this.dbPropertiesFile = path;
	}

}
