package com.dsa.pcapneo.service;

public class LocalGraphDatabase implements GraphDatabase {
	EmbeddedGraphDatabase graphDb;
	GraphDatabaseFactory() graphDbFactory;

	@Override
	public void open() {
		this.graphDb = new GraphDatabaseFactory().newEmbeddedDatabase()
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
